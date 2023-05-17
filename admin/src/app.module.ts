import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User, validateUser } from './entity/user.entity';
import { SnakeNamingStrategy } from 'typeorm-naming-strategies';
import { Course, validateCourse } from './entity/course.entity';
import { Location } from './entity/location.entity';
import {
  CourseReview,
  validateCourseReview,
} from './entity/course-review.entity';
import { ConfigModule } from '@nestjs/config';

const authenticate = async (email: string, password: string) => {
  if (
    email === process.env.ODE_SEOUL_ADMIN_USERNAME &&
    password === process.env.ODE_SEOUL_ADMIN_PASSWORD
  ) {
    return Promise.resolve({
      email: process.env.ODE_SEOUL_ADMIN_USERNAME,
      password: process.env.ODE_SEOUL_ADMIN_PASSWORD,
    });
  }

  return null;
};

const createResource = (
  entity: any,
  primaryColumnName: string,
  readOnly: boolean,
  saveValidator?: (request, response) => any,
  properties?: {},
) => {
  return {
    resource: entity,
    options: {
      actions: {
        new: {
          isAccessible: !readOnly,
          before: [
            (request, response) => {
              if (request.method !== 'post' || !saveValidator) {
                return request;
              }

              return saveValidator(request, response);
            },
          ],
        },
        edit: {
          isAccessible: !readOnly,
          before: [
            (request, response) => {
              if (request.method !== 'post' || !saveValidator) {
                return request;
              }

              return saveValidator(request, response);
            },
          ],
        },
        delete: {
          isAccessible: !readOnly,
          handler: async (request, response, context) => {
            const { record, resource, currentAdmin, h } = context;
            const { params } = record;

            const instance = await entity.findOneBy({
              [primaryColumnName]: params[primaryColumnName],
            });
            await instance.delete();

            return {
              record: record.toJSON(currentAdmin),
              redirectUrl: h.resourceUrl({
                resourceId: resource._decorated?.id() || resource.id(),
              }),
              notice: {
                message: 'successfullyDeleted',
                type: 'success',
              },
            };
          },
        },
        bulkDelete: {
          isAccessible: false,
        },
      },
      properties: {
        ...properties,
        createdAt: {
          isVisible: {
            show: true,
            list: true,
            edit: false,
            filter: true,
          },
          position: 1000,
        },
        updatedAt: {
          isVisible: {
            show: true,
            list: true,
            edit: false,
            filter: true,
          },
          position: 1001,
        },
        deletedAt: {
          isVisible: false,
        },
      },
    },
  };
};

@Module({
  imports: [
    ConfigModule.forRoot(),
    TypeOrmModule.forRoot({
      type: 'mysql',
      host: process.env.ODE_SEOUL_DB_HOSTNAME,
      port: Number(process.env.ODE_SEOUL_DB_PORT),
      username: process.env.ODE_SEOUL_DB_USERNAME,
      password: process.env.ODE_SEOUL_DB_PASSWORD,
      database: process.env.ODE_SEOUL_DB_SCHEMA,
      entities: [User, Course, CourseReview, Location],
      synchronize: false,
      ssl: {
        rejectUnauthorized: true,
      },
      namingStrategy: new SnakeNamingStrategy(),
      legacySpatialSupport: false,
    }),
    import('@adminjs/nestjs').then(async ({ AdminModule }) => {
      const AdminJSTypeOrm = await import('@adminjs/typeorm');
      const AdminJS = (await import('adminjs')).default;

      const componentLoader = new (await import('adminjs')).ComponentLoader();
      const components = {
        Dashboard: componentLoader.add('Dashboard', './component/Dashboard'),
      };

      AdminJS.registerAdapter({
        Database: AdminJSTypeOrm.Database,
        Resource: AdminJSTypeOrm.Resource,
      });

      return AdminModule.createAdminAsync({
        useFactory: async () => ({
          adminJsOptions: {
            componentLoader,
            dashboard: {
              component: components.Dashboard,
            },
            resources: [
              createResource(User, 'id', false, validateUser, {
                nickname: {
                  isTitle: true,
                },
              }),
              createResource(Course, 'id', false, validateCourse, {
                name: {
                  isTitle: true,
                },
                id: {
                  isVisible: true,
                },
                categories: {
                  type: 'string',
                  isArray: true,
                  availableValues: [
                    { value: 'SCENERY', label: '볼거리가 많은' },
                    { value: 'DATE', label: '데이트 코스' },
                    { value: 'NATURE', label: '자연을 즐기는' },
                    { value: 'RUN', label: '러닝' },
                    { value: 'WALK', label: '간단한 산책' },
                    { value: 'CARE', label: '마음을 정리하고 싶을 때' },
                    { value: 'RELAX', label: '커피 마시면서 여유롭게' },
                  ],
                },
                description: {
                  type: 'textarea',
                },
                accessWay: {
                  type: 'textarea',
                },
              }),
              createResource(Location, 'code', true, undefined, {
                code: {
                  isTitle: true,
                },
              }),
              createResource(CourseReview, 'id', false, validateCourseReview, {
                id: {
                  isTitle: true,
                },
                content: {
                  type: 'textarea',
                },
              }),
            ],
          },
          auth:
            process.env.NODE_ENV === 'development'
              ? undefined
              : {
                  authenticate,
                  cookieName: 'ode_seoul_adminjs',
                  cookiePassword: process.env.ODE_SEOUL_ADMIN_SECRET,
                },
          sessionOptions:
            process.env.NODE_ENV === 'development'
              ? undefined
              : {
                  resave: true,
                  saveUninitialized: true,
                  secret: process.env.ODE_SEOUL_ADMIN_SECRET,
                },
        }),
      });
    }),
  ],
  controllers: [],
  providers: [],
})
export class AppModule {}

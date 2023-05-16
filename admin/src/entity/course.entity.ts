import { Column, Entity, PrimaryColumn } from 'typeorm';
import { CommonEntity } from './common/common.entity';

enum CourseRegion {
  NORTH = 'NORTH',
  SOUTH = 'SOUTH',
}

@Entity({ name: 'course' })
export class Course extends CommonEntity {
  @PrimaryColumn()
  public id: number;

  @Column({ length: 45 })
  public name: string;

  @Column({ type: 'int' })
  public level: number;

  @Column({ type: 'int' })
  public distance: number;

  @Column({ type: 'int' })
  public time: number;

  @Column({ type: 'text' })
  public description: string;

  @Column({ type: 'json' })
  public categories: string[];

  @Column({ length: 45 })
  public gugunSummary: string;

  @Column({ length: 500 })
  public routeSummary: string;

  @Column({ length: 45 })
  public nearSubway: string;

  @Column({ length: 500 })
  public accessWay: string;

  @Column({ type: 'enum', enum: CourseRegion })
  public region: CourseRegion;

  @Column({ type: 'multilinestring', srid: 4326 })
  public route: string;

  @Column({ length: 500 })
  public image: string;
}

export const validateCourse = async (request, context) => {
  const { payload } = request;
  const errors: {
    [key: string]: {
      message: string;
    };
  } = {};

  payload.name ??= '';
  payload.level ??= 0;
  payload.distance ??= 0;
  payload.time ??= 0;
  payload.description ??= '';
  payload.gugunSummary ??= '';
  payload.routeSummary ??= '';
  payload.nearSubway ??= '';
  payload.accessWay ??= '';
  payload.image ??= '';

  for (const [key, value] of Object.entries(payload)) {
    if (key.startsWith('categories')) {
      if (!value) {
        errors[key] = {
          message: 'cannot be empty',
        };
      }
    }
  }

  if (!Number(payload.id)) {
    errors.id = {
      message: 'a number required',
    };
  }

  if (!payload.region) {
    errors.region = {
      message: 'required',
    };
  }

  if (Object.keys(errors).length) {
    throw new (await import('adminjs')).ValidationError(errors);
  }

  return request;
};

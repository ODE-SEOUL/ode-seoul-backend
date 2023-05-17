import { Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { CommonEntity } from './common/common.entity';
import { User } from './user.entity';
import { Course } from './course.entity';

@Entity({ name: 'user_picked_course' })
export class UserPickedCourse extends CommonEntity {
  @PrimaryGeneratedColumn()
  public id: number;

  @ManyToOne((type) => User, {
    eager: true,
  })
  public user: User;

  @ManyToOne((type) => Course, {
    eager: true,
  })
  public course: Course;
}

export const validateUserPickedCourse = async (request, context) => {
  const { payload } = request;
  const errors: {
    [key: string]: {
      message: string;
    };
  } = {};

  if (!payload['user.id']) {
    errors['user.id'] = {
      message: 'required',
    };
  }

  if (!payload['course.id']) {
    errors['course.id'] = {
      message: 'required',
    };
  }

  if (Object.keys(errors).length) {
    throw new (await import('adminjs')).ValidationError(errors);
  }

  return request;
};

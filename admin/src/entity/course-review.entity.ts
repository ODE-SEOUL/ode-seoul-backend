import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { CommonEntity } from './common/common.entity';
import { User } from './user.entity';
import { Course } from './course.entity';

@Entity({ name: 'course_review' })
export class CourseReview extends CommonEntity {
  @PrimaryGeneratedColumn()
  public id: number;

  @ManyToOne((type) => Course, {
    eager: true,
  })
  public course: Course;

  @ManyToOne((type) => User, {
    eager: true,
  })
  public user: User;

  @Column({ type: 'int' })
  public score: number;

  @Column({ type: 'text' })
  public content: string;

  @Column({ length: 500 })
  public image: string;
}

export const validateCourseReview = async (request, context) => {
  const { payload } = request;
  const errors: {
    [key: string]: {
      message: string;
    };
  } = {};

  payload.score ??= 0;
  payload.content ??= '';
  payload.image ??= '';

  if (Object.keys(errors).length) {
    throw new (await import('adminjs')).ValidationError(errors);
  }

  return request;
};

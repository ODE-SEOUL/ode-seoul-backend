import {
  Column,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { CommonEntity } from './common/common.entity';
import { User } from './user.entity';
import { Course } from './course.entity';

enum RecruitCategory {
  COM_ANIMAL = 'COM_ANIMAL',
  COM_HOUSE = 'COM_HOUSE',
  COM_OFFICE = 'COM_OFFICE',
  COM_NEIGHBOR = 'COM_NEIGHBOR',
  COM_EXERCISE = 'COM_EXERCISE',
  COM_PHOTO = 'COM_PHOTO',
  COM_EXPER = 'COM_EXPER',
}

enum RecruitProgressStatus {
  OPEN = 'OPEN',
  CLOSED = 'CLOSED',
  DONE = 'DONE',
}

@Entity({ name: 'recruit' })
export class Recruit extends CommonEntity {
  @PrimaryGeneratedColumn()
  public id: number;

  @ManyToOne((type) => User, {
    eager: true,
  })
  @JoinColumn({ name: 'host_user_id' })
  public host: User;

  @ManyToOne((type) => Course, {
    eager: true,
  })
  public course: Course;

  @Column({ type: 'enum', enum: RecruitCategory })
  public category: RecruitCategory;

  @Column({ length: 500 })
  public title: string;

  @Column({ type: 'text' })
  public content: string;

  @Column({ length: 500 })
  public image: string;

  @Column({ type: 'int' })
  public currentPeople: number;

  @Column({ type: 'int' })
  public maxPeople: number;

  @Column()
  public scheduledAt: Date;

  @Column({ type: 'enum', enum: RecruitProgressStatus })
  public progressStatus: RecruitProgressStatus;
}

export const validateRecruit = async (request, context) => {
  const { payload } = request;
  const errors: {
    [key: string]: {
      message: string;
    };
  } = {};

  payload.title ??= '';
  payload.content ??= '';
  payload.image ??= '';
  payload.currentPeople ??= 0;
  payload.maxPeople ??= 0;

  if (payload.currentPeople < 1) {
    payload.currentPeople = 1;
  }

  if (payload.maxPeople < 0) {
    payload.maxPeople = 0;
  }

  if (!payload['host.id']) {
    errors['host.id'] = {
      message: 'required',
    };
  }

  if (!payload['course.id']) {
    errors['course.id'] = {
      message: 'required',
    };
  }

  if (!payload['category']) {
    errors['category'] = {
      message: 'required',
    };
  }

  if (!payload['scheduledAt']) {
    errors['scheduledAt'] = {
      message: 'required',
    };
  }

  if (!payload['progressStatus']) {
    errors['progressStatus'] = {
      message: 'required',
    };
  }

  if (Object.keys(errors).length) {
    throw new (await import('adminjs')).ValidationError(errors);
  }

  return request;
};

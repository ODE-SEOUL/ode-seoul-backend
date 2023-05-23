import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { CommonEntity } from './common/common.entity';
import { User } from './user.entity';
import { Recruit } from './recruit.entity';

@Entity({ name: 'recruit_comment' })
export class RecruitComment extends CommonEntity {
  @PrimaryGeneratedColumn()
  public id: number;

  @ManyToOne((type) => Recruit, {
    eager: true,
  })
  public recruit: Recruit;

  @ManyToOne((type) => User, {
    eager: true,
  })
  public user: User;

  @Column({ type: 'text' })
  public content: string;
}

export const validateRecruitComment = async (request, context) => {
  const { payload } = request;
  const errors: {
    [key: string]: {
      message: string;
    };
  } = {};

  payload.content ??= '';

  if (!payload['recruit.id']) {
    errors['recruit.id'] = {
      message: 'required',
    };
  }

  if (!payload['user.id']) {
    errors['user.id'] = {
      message: 'required',
    };
  }

  if (Object.keys(errors).length) {
    throw new (await import('adminjs')).ValidationError(errors);
  }

  return request;
};

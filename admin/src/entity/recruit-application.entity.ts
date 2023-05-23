import { Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { CommonEntity } from './common/common.entity';
import { User } from './user.entity';
import { Recruit } from './recruit.entity';

@Entity({ name: 'recruit_application' })
export class RecruitApplication extends CommonEntity {
  @PrimaryGeneratedColumn()
  public id: number;

  @ManyToOne((type) => Recruit, {
    eager: true,
  })
  public recruit: Recruit;

  @ManyToOne((type) => User, {
    eager: true,
  })
  @JoinColumn({ name: 'member_user_id' })
  public member: User;
}

export const validateRecruitApplication = async (request, context) => {
  const { payload } = request;
  const errors: {
    [key: string]: {
      message: string;
    };
  } = {};

  if (!payload['recruit.id']) {
    errors['recruit.id'] = {
      message: 'required',
    };
  }

  if (!payload['member.id']) {
    errors['member.id'] = {
      message: 'required',
    };
  }

  if (Object.keys(errors).length) {
    throw new (await import('adminjs')).ValidationError(errors);
  }

  return request;
};

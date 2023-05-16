import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { CommonEntity } from './common/common.entity';
import { Location } from './location.entity';

enum SignupStatus {
  BEFORE_REGISTERED = 'BEFORE_REGISTERED',
  REGISTERED = 'REGISTERED',
}

@Entity({ name: 'user' })
export class User extends CommonEntity {
  @PrimaryGeneratedColumn()
  public id: number;

  @ManyToOne((type) => Location, {
    nullable: true,
    eager: true,
  })
  location: Location | null;

  @Column({ length: 45 })
  public nickname: string;

  @Column({ length: 500 })
  public profileImage: string;

  @Column({ length: 45, nullable: true })
  public locationCode: string | null;

  @Column({ length: 45 })
  public loginId: string;

  @Column({ length: 500 })
  public loginPw: string;

  @Column({ length: 500 })
  public refreshToken: string;

  @Column({ type: 'enum', enum: SignupStatus })
  public signupStatus: SignupStatus;
}

export const validateUser = async (request, context) => {
  const { payload } = request;
  const errors: {
    [key: string]: {
      message: string;
    };
  } = {};

  payload.nickname ??= '';
  payload.profileImage ??= '';
  payload.loginId ??= '';
  payload.loginPw ??= '';
  payload.refreshToken ??= '';

  if (!payload.signupStatus) {
    errors.signupStatus = {
      message: 'required',
    };
  }

  if (Object.keys(errors).length) {
    throw new (await import('adminjs')).ValidationError(errors);
  }

  return request;
};

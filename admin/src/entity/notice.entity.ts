import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';
import { CommonEntity } from './common/common.entity';

@Entity({ name: 'notice' })
export class Notice extends CommonEntity {
  @PrimaryGeneratedColumn()
  public id: number;

  @Column({ length: 45 })
  public author: string;

  @Column({ length: 500 })
  public title: string;

  @Column({ type: 'text' })
  public content: string;
}

export const validateNotice = async (request, context) => {
  const { payload } = request;
  const errors: {
    [key: string]: {
      message: string;
    };
  } = {};

  payload.author ??= '';
  payload.title ??= '';
  payload.content ??= '';

  if (Object.keys(errors).length) {
    throw new (await import('adminjs')).ValidationError(errors);
  }

  return request;
};

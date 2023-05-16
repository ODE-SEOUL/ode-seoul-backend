import { Column, Entity, PrimaryColumn } from 'typeorm';
import { CommonEntity } from './common/common.entity';

@Entity({ name: 'location' })
export class Location extends CommonEntity {
  @PrimaryColumn({ length: 45 })
  public code: string;

  @Column({ length: 45 })
  public address1: string;

  @Column({ length: 45 })
  public address2: string;

  @Column({ length: 45 })
  public address3: string;

  @Column({ type: 'double' })
  public latitude: number;

  @Column({ type: 'double' })
  public longitude: number;

  @Column({ type: 'tinyint' })
  public seoulGugun: boolean;
}

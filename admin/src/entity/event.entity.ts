import {
  BaseEntity,
  BeforeInsert,
  Column,
  CreateDateColumn,
  Entity,
  PrimaryGeneratedColumn,
} from 'typeorm';

@Entity({ name: 'event' })
export class Event extends BaseEntity {
  @PrimaryGeneratedColumn()
  public id: number;

  @Column({ length: 45 })
  public uuid: string;

  @Column({ length: 45 })
  public codename: string;

  @Column({ length: 45 })
  public guname: string;

  @Column({ length: 500 })
  public title: string;

  @Column({ length: 500 })
  public place: string;

  @Column({ length: 500 })
  public useTarget: string;

  @Column({ length: 500 })
  public useFee: string;

  @Column({ length: 500 })
  public orgLink: string;

  @Column({ length: 500 })
  public mainImage: string;

  @Column({ type: 'date' })
  public startDate: Date;

  @Column({ type: 'date' })
  public endDate: Date;

  @Column({ type: 'date' })
  public registerDate: Date;

  @CreateDateColumn({ nullable: true })
  public createdAt: Date | null;

  @BeforeInsert()
  public beforeInsert() {
    this.createdAt = new Date();
  }
}

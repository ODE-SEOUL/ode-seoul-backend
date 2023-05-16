import {
  BaseEntity,
  CreateDateColumn,
  UpdateDateColumn,
  DeleteDateColumn,
  BeforeInsert,
  BeforeUpdate,
} from 'typeorm';

export class CommonEntity extends BaseEntity {
  @CreateDateColumn({ nullable: true })
  public createdAt: Date | null;

  @UpdateDateColumn({ nullable: true })
  public updatedAt: Date | null;

  @DeleteDateColumn({ nullable: true })
  public deletedAt: Date | null;

  public delete() {
    this.deletedAt = new Date();
    return this.save();
  }

  @BeforeInsert()
  public beforeInsert() {
    this.createdAt = new Date();
  }

  @BeforeUpdate()
  public beforeUpdate() {
    this.updatedAt = new Date();
  }
}

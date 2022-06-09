import { Moment } from 'moment';
import { PermissionType } from 'app/shared/model/enumerations/permission-type.model';
import { EntityClass } from 'app/shared/model/enumerations/entity-class.model';

export interface ITemporaryAccess {
  id?: number;
  login?: string;
  endDate?: Moment;
  permissionType?: PermissionType;
  entityClass?: EntityClass;
  entityId?: number;
}

export class TemporaryAccess implements ITemporaryAccess {
  constructor(
    public id?: number,
    public login?: string,
    public endDate?: Moment,
    public permissionType?: PermissionType,
    public entityClass?: EntityClass,
    public entityId?: number
  ) {}
}

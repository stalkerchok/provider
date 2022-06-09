import { Moment } from 'moment';
import { PermissionType } from 'app/shared/model/enumerations/permission-type.model';
import { Role } from 'app/shared/model/enumerations/role.model';
import { EntityClass } from 'app/shared/model/enumerations/entity-class.model';

export interface ITemporaryAccess {
  id?: number;
  login?: string;
  endDate?: Moment;
  permissionType?: PermissionType;
  role?: Role;
  entityClass?: EntityClass;
  entityId?: number;
}

export class TemporaryAccess implements ITemporaryAccess {
  constructor(
    public id?: number,
    public login?: string,
    public endDate?: Moment,
    public permissionType?: PermissionType,
    public role?: Role,
    public entityClass?: EntityClass,
    public entityId?: number
  ) {}
}

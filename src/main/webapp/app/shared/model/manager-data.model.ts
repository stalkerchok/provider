export interface IManagerData {
  id?: number;
  name?: string;
  data?: string;
}

export class ManagerData implements IManagerData {
  constructor(public id?: number, public name?: string, public data?: string) {}
}

export interface IExecutorData {
  id?: number;
  name?: string;
  data?: string;
}

export class ExecutorData implements IExecutorData {
  constructor(public id?: number, public name?: string, public data?: string) {}
}

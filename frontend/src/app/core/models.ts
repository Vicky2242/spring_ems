export interface RoleSummary { id:number; name:string; }
export interface Employee { id:number; firstName:string; lastName:string; email:string; phone:string|null; joiningDate:string|null; departmentId:number; departmentName:string; roles:RoleSummary[]; }
export interface EmployeeRequest { firstName:string; lastName:string; email:string; phone:string|null; joiningDate:string|null; departmentId:number; roleIds:number[]; }
export interface Department { id:number; name:string; description:string|null; }
export interface Role { id:number; name:string; description:string|null; }
export interface ApiError { timestamp?:string; status?:number; error?:string; message?:string; validationErrors?:Record<string,string>; }

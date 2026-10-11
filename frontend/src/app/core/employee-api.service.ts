import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Department, Employee, EmployeeRequest, Role } from './models';
@Injectable({providedIn:'root'})
export class EmployeeApiService {
 private readonly http=inject(HttpClient); private readonly baseUrl='/api';
 getEmployees():Observable<Employee[]>{return this.http.get<Employee[]>(`${this.baseUrl}/employees`);}
 getEmployee(id:number):Observable<Employee>{return this.http.get<Employee>(`${this.baseUrl}/employees/${id}`);}
 createEmployee(request:EmployeeRequest):Observable<Employee>{return this.http.post<Employee>(`${this.baseUrl}/employees`,request);}
 updateEmployee(id:number,request:EmployeeRequest):Observable<Employee>{return this.http.put<Employee>(`${this.baseUrl}/employees/${id}`,request);}
 deleteEmployee(id:number):Observable<void>{return this.http.delete<void>(`${this.baseUrl}/employees/${id}`);}
 getDepartments():Observable<Department[]>{return this.http.get<Department[]>(`${this.baseUrl}/departments`);}
 getRoles():Observable<Role[]>{return this.http.get<Role[]>(`${this.baseUrl}/roles`);}
}

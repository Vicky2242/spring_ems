import { Component, OnInit, inject } from '@angular/core';
import { DatePipe, NgFor, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { EmployeeApiService } from '../../core/employee-api.service';
import { ApiError, Employee } from '../../core/models';
@Component({standalone:true,selector:'app-employee-list',imports:[DatePipe,NgFor,NgIf,RouterLink],template:`
<section class="page-heading"><div><p class="eyebrow">PEOPLE OPERATIONS</p><h1>Employees</h1><p class="muted">Manage employee records, departments, and assigned roles.</p></div><a class="button primary" routerLink="/employees/new">+ Add employee</a></section>
<div class="notice error" *ngIf="error">{{error}}</div><div class="notice success" *ngIf="success">{{success}}</div>
<section class="panel"><div class="panel-header"><div><h2>Employee directory</h2><p class="muted">{{employees.length}} record(s)</p></div><button class="button secondary" type="button" (click)="loadEmployees()">Refresh</button></div>
<div class="loading" *ngIf="loading">Loading employees…</div><div class="empty-state" *ngIf="!loading && employees.length===0"><h3>No employees yet</h3><p>Create a department and role through the API, then add your first employee.</p><a class="button primary" routerLink="/employees/new">Add first employee</a></div>
<div class="table-wrap" *ngIf="!loading && employees.length>0"><table><thead><tr><th>Employee</th><th>Department</th><th>Roles</th><th>Joining date</th><th>Actions</th></tr></thead><tbody><tr *ngFor="let e of employees"><td><div class="person-cell"><span class="avatar">{{e.firstName.charAt(0)}}{{e.lastName.charAt(0)}}</span><span><strong>{{e.firstName}} {{e.lastName}}</strong><small>{{e.email}}</small></span></div></td><td>{{e.departmentName}}</td><td><span class="tag" *ngFor="let role of e.roles">{{role.name}}</span><span *ngIf="e.roles.length===0" class="muted">—</span></td><td>{{e.joiningDate ? (e.joiningDate | date:'mediumDate') : '—'}}</td><td class="actions"><a class="text-action" [routerLink]="['/employees',e.id,'edit']">Edit</a><button class="text-action danger-text" type="button" (click)="deleteEmployee(e)">Delete</button></td></tr></tbody></table></div></section>`})
export class EmployeeListComponent implements OnInit {
 private readonly api=inject(EmployeeApiService); employees:Employee[]=[]; loading=true; error=''; success='';
 ngOnInit():void{this.loadEmployees();}
 loadEmployees():void{this.loading=true;this.error='';this.api.getEmployees().subscribe({next:data=>{this.employees=data;this.loading=false;},error:err=>{this.error=this.message(err);this.loading=false;}});}
 deleteEmployee(e:Employee):void{if(!window.confirm(`Delete ${e.firstName} ${e.lastName}? This cannot be undone.`))return;this.api.deleteEmployee(e.id).subscribe({next:()=>{this.success='Employee deleted successfully.';this.loadEmployees();},error:err=>this.error=this.message(err)});}
 private message(err:HttpErrorResponse):string{return (err.error as ApiError|null)?.message??'Could not connect to the API. Check that Spring Boot is running.';}
}

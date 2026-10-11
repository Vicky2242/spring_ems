import { Component, OnInit, inject } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { EmployeeApiService } from '../../core/employee-api.service';
import { ApiError, Department, Employee, EmployeeRequest, Role } from '../../core/models';
@Component({standalone:true,selector:'app-employee-form',imports:[NgFor,NgIf,ReactiveFormsModule,RouterLink],template:`
<section class="page-heading"><div><p class="eyebrow">PEOPLE OPERATIONS</p><h1>{{isEdit?'Edit employee':'Add employee'}}</h1><p class="muted">Enter employee details and assign a department and roles.</p></div><a class="button secondary" routerLink="/employees">Back to directory</a></section>
<div class="notice error" *ngIf="error">{{error}}</div><section class="panel form-panel" *ngIf="!loading"><form [formGroup]="form" (ngSubmit)="save()"><h2>Personal information</h2><div class="form-grid">
<label>First name<input formControlName="firstName" placeholder="e.g. Asha"><small class="field-error" *ngIf="form.controls.firstName.touched&&form.controls.firstName.invalid">Enter a first name (max 80 characters).</small></label>
<label>Last name<input formControlName="lastName" placeholder="e.g. Kumar"><small class="field-error" *ngIf="form.controls.lastName.touched&&form.controls.lastName.invalid">Enter a last name (max 80 characters).</small></label>
<label>Email address<input type="email" formControlName="email" placeholder="asha@example.com"><small class="field-error" *ngIf="form.controls.email.touched&&form.controls.email.invalid">Enter a valid email address.</small></label>
<label>Phone number<input type="tel" formControlName="phone" placeholder="Optional phone number"></label>
<label>Joining date<input type="date" formControlName="joiningDate"></label>
<label>Department<select formControlName="departmentId"><option value="">Select a department</option><option *ngFor="let d of departments" [value]="d.id">{{d.name}}</option></select><small class="field-error" *ngIf="departments.length===0">No departments found. Create one using POST /api/departments.</small></label></div>
<h2 class="section-title">Assigned roles</h2><div class="role-options" *ngIf="roles.length>0"><label class="check-option" *ngFor="let role of roles"><input type="checkbox" [checked]="selectedRoleIds.includes(role.id)" (change)="toggleRole(role.id,$any($event.target).checked)"><span>{{role.name}}</span></label></div><p class="muted" *ngIf="roles.length===0">No roles found. Create one using POST /api/roles. Roles are optional.</p>
<div class="form-actions"><a class="button secondary" routerLink="/employees">Cancel</a><button class="button primary" type="submit" [disabled]="saving">{{saving?'Saving…':(isEdit?'Save changes':'Create employee')}}</button></div></form></section><div class="loading" *ngIf="loading">Loading form…</div>`})
export class EmployeeFormComponent implements OnInit {
 private readonly fb=inject(FormBuilder);private readonly api=inject(EmployeeApiService);private readonly route=inject(ActivatedRoute);private readonly router=inject(Router);
 departments:Department[]=[];roles:Role[]=[];selectedRoleIds:number[]=[];loading=true;saving=false;error='';employeeId:number|null=null;
 form=this.fb.nonNullable.group({firstName:['',[Validators.required,Validators.maxLength(80)]],lastName:['',[Validators.required,Validators.maxLength(80)]],email:['',[Validators.required,Validators.email,Validators.maxLength(150)]],phone:['',[Validators.maxLength(20)]],joiningDate:[''],departmentId:['',Validators.required]});
 get isEdit():boolean{return this.employeeId!==null;}
 ngOnInit():void{const id=this.route.snapshot.paramMap.get('id');this.employeeId=id?Number(id):null;forkJoin({departments:this.api.getDepartments(),roles:this.api.getRoles()}).subscribe({next:data=>{this.departments=data.departments;this.roles=data.roles;if(this.employeeId!==null){this.api.getEmployee(this.employeeId).subscribe({next:e=>{this.patch(e);this.loading=false;},error:err=>{this.error=this.message(err);this.loading=false;}});}else this.loading=false;},error:err=>{this.error=this.message(err);this.loading=false;}});}
 toggleRole(id:number,checked:boolean):void{this.selectedRoleIds=checked?[...this.selectedRoleIds,id]:this.selectedRoleIds.filter(value=>value!==id);}
 save():void{this.form.markAllAsTouched();if(this.form.invalid)return;this.saving=true;this.error='';const v=this.form.getRawValue();const request:EmployeeRequest={firstName:v.firstName.trim(),lastName:v.lastName.trim(),email:v.email.trim(),phone:v.phone.trim()||null,joiningDate:v.joiningDate||null,departmentId:Number(v.departmentId),roleIds:this.selectedRoleIds};const call=this.employeeId===null?this.api.createEmployee(request):this.api.updateEmployee(this.employeeId,request);call.subscribe({next:()=>this.router.navigate(['/employees']),error:err=>{this.error=this.message(err);this.saving=false;}});}
 private patch(e:Employee):void{this.form.patchValue({firstName:e.firstName,lastName:e.lastName,email:e.email,phone:e.phone??'',joiningDate:e.joiningDate??'',departmentId:String(e.departmentId)});this.selectedRoleIds=e.roles.map(role=>role.id);}
 private message(err:HttpErrorResponse):string{const body=err.error as ApiError|null;if(body?.validationErrors)return Object.entries(body.validationErrors).map(([field,msg])=>`${field}: ${msg}`).join(' · ');return body?.message??'Request failed. Check the backend and try again.';}
}

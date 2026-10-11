import { Routes } from '@angular/router';
import { EmployeeListComponent } from './features/employees/employee-list.component';
import { EmployeeFormComponent } from './features/employees/employee-form.component';
export const routes: Routes = [
 {path:'',pathMatch:'full',redirectTo:'employees'},
 {path:'employees',component:EmployeeListComponent},
 {path:'employees/new',component:EmployeeFormComponent},
 {path:'employees/:id/edit',component:EmployeeFormComponent},
 {path:'**',redirectTo:'employees'}
];

import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
@Component({selector:'app-root',standalone:true,imports:[RouterLink,RouterOutlet],template:`
<header class="topbar"><a class="brand" routerLink="/employees">People<span>Desk</span></a><nav><a routerLink="/employees">Employees</a></nav></header>
<main class="page-shell"><router-outlet /></main><footer class="footer">Employee Management System · Learning project</footer>`})
export class AppComponent {}

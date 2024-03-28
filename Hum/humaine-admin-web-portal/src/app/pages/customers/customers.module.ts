
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomersComponent } from './customers.component';
import { CustomerRoutingModule } from './customers-routing.module';
import { CoreModule } from 'src/app/_core/core.module';



@NgModule({
  declarations: [CustomersComponent],
  imports: [
    CommonModule,
    CustomerRoutingModule,
    CoreModule 
  ]
})
export class CustomersModule { }

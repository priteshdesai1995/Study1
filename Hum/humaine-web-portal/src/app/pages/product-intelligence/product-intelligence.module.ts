import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductIntelligenceComponent } from './product-intelligence.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../core/_common/shared.module';
import { CoreModule } from '../../core/core.module';
import { NgSelectModule } from '@ng-select/ng-select';




const routes: Routes = [
  {
    path: '',
    component: ProductIntelligenceComponent
  }
];

@NgModule({
  declarations: [
    ProductIntelligenceComponent
  ],
  imports: [
    CommonModule,
    CoreModule,
    SharedModule,
    NgSelectModule,
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class ProductIntelligenceModule { }

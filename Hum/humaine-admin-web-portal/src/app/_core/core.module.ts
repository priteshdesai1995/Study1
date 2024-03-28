import { NumberFormatPipe } from './_common/number-format.pipe';
import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NgxMasonryModule } from "ngx-masonry";
import { NzCollapseModule } from 'ng-zorro-antd/collapse';
import { LoaderDirective } from "./_directive/loader.directive";
import { SharedModule } from "./_common/shared.module";
import { ListCustomerComponent } from './_component/customer/list-customer/list-customer/list-customer.component';
import { ListTableComponent } from "./_component/list-table/list-table.component";
import { EditCustomerComponent } from './_component/customer/edit-customer/edit-customer.component';
import { NgSelectComponent, NgSelectModule } from "@ng-select/ng-select";
import { RouterModule } from "@angular/router";

@NgModule({
    declarations: [LoaderDirective, ListTableComponent, ListCustomerComponent, EditCustomerComponent, NumberFormatPipe
    ],
    exports: [
        LoaderDirective,
        ListTableComponent,
        ListCustomerComponent,
        EditCustomerComponent,
        NumberFormatPipe
    ],
    imports: [
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
        NzSpinModule,
        NzCollapseModule,
        NgxMasonryModule,
        SharedModule,
        NgSelectModule,
        RouterModule,
    ]})  
export class CoreModule { }

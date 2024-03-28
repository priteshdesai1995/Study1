import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NzAutocompleteModule } from 'ng-zorro-antd/auto-complete';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzBreadCrumbModule } from 'ng-zorro-antd/breadcrumb';
import { NzPageHeaderModule } from 'ng-zorro-antd/page-header';
import { NzListModule } from 'ng-zorro-antd/list';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzSwitchModule } from 'ng-zorro-antd/switch';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzCollapseModule } from 'ng-zorro-antd/collapse';
import { NzNotificationModule } from 'ng-zorro-antd/notification';
import { NzDescriptionsModule } from 'ng-zorro-antd/descriptions';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
// import { NzPageHeaderModule } from 'ng-zorro-antd/page-header';
import { NzPaginationModule } from 'ng-zorro-antd/pagination';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzStatisticModule } from 'ng-zorro-antd/statistic';
import { NzCheckboxModule } from 'ng-zorro-antd/checkbox';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzUploadModule } from 'ng-zorro-antd/upload';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NgSelectModule } from '@ng-select/ng-select';
import { NzPopconfirmModule } from 'ng-zorro-antd/popconfirm';
import { NzSliderModule } from 'ng-zorro-antd/slider';
import { NzSpinComponent, NzSpinModule } from 'ng-zorro-antd/spin';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { LoaderDirective } from '../_directive/loader.directive';


const BASE_MODULES = [CommonModule, FormsModule, ReactiveFormsModule];

const NZ_MODULES = [
    NzPageHeaderModule,
    NzGridModule,
    NzEmptyModule,
    NzIconModule,
    NzBreadCrumbModule,
    NzListModule,
    NzFormModule,
    NzCardModule,
    NzTagModule,
    NzSwitchModule,
    NzTabsModule,
    NzModalModule,
    NzUploadModule,
    NzTableModule,
    NzSelectModule,
    NzDescriptionsModule,
    NzButtonModule,
    NzNotificationModule,
    NzAutocompleteModule,
    NzCollapseModule,
    NzAvatarModule,
    NzDropDownModule,
    // NzPageHeaderModule,
    NzPaginationModule,
    NzRadioModule,
    NzBadgeModule,
    NzStatisticModule,
    NzCheckboxModule,
    NzDividerModule,
    NzPopconfirmModule,
    NzToolTipModule,
    NzSliderModule,
    NzSpinModule
];

@NgModule({
    declarations: [],
    imports: [...BASE_MODULES, ...NZ_MODULES,NgSelectModule],
    exports: [...BASE_MODULES, ...NZ_MODULES],
    // entryComponents: [...ENTRY_COMPONENTS],
})

export class SharedModule { }

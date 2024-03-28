import { SortDirection } from './../_constant/app-constant';
import { DataTableColumn } from './DataTableColumn';
import { APIType } from './APIType';
export class DataTableConfig {
  listAPI: string;
  listAPIType: APIType;
  deleteAPI: string;
  title: string;
  columns: DataTableColumn[];
  paginate: boolean = false;
  viewPersonaPath: string;
  sortFields: { name: string; value: string }[];
  showCheckBoxColumn: boolean = false;
  defaultSorting: string = 'id';
  defaultSortingDirection: SortDirection = SortDirection.ASC;
  deleteOPopupTitle: string;
  deletePopupSubTitle: string;
  deletePopupButtonText: string;
  renderFromMockData: boolean = false;
  showRegenerate: boolean = false;
  showSave: boolean = false;
  isScroll: boolean = false;
  scrollHeight: string = "550px";
  scrollWidth: string = "1000px";
  isAllowDelete: boolean = true;
  isTitle: Boolean = true;
  isAllowEdit: boolean = false;
  deleteMultipleAPI: string = '';
  deleteSelPopupTitle: string = '';
  deleteSelPopupSubTitle: string = '';
  deleteSelPopupButtonText: string = '';
  constructor(
    listAPI: string,
    listAPIType: APIType,
    deleteAPI: string,
    viewPersonaPath: string,
    title: string,
    columns: DataTableColumn[],
    sortFields: { name: string; value: string }[],
    deleteOPopupTitle: string,
    deletePopupSubTitle: string,
    deletePopupButtonText: string,
    defaultSorting: string = 'id',
    defaultSortingDirection: SortDirection = SortDirection.ASC,
    renderFromMockData: boolean = false,
    paginate: boolean = false,
    showCheckBoxColumn: boolean = false,
    showRegenerate: boolean = false,
    showSave: boolean = false,
    isScroll: boolean = false,
    scrollHeight: string = "500px",
    scrollWidth: string = "1000px",
    isAllowDelete: boolean = true,
    isAllowEdit: boolean = false,
    isTitle: boolean = false,
    deleteMultipleAPI: string = '',
    deleteSelPopupTitle: string = '',
    deleteSelPopupSubTitle: string = '',
    deleteSelPopupButtonText: string = ''

  ) {
    this.listAPI = listAPI;
    this.listAPIType = listAPIType;
    this.deleteAPI = deleteAPI;
    this.viewPersonaPath = viewPersonaPath;
    this.title = title;
    this.columns = columns;
    this.sortFields = sortFields;
    this.paginate = paginate;
    this.showCheckBoxColumn = showCheckBoxColumn;
    this.defaultSorting = defaultSorting;
    this.deleteOPopupTitle = deleteOPopupTitle;
    this.deletePopupSubTitle = deletePopupSubTitle;
    this.deletePopupButtonText = deletePopupButtonText;
    this.renderFromMockData = renderFromMockData;
    this.showRegenerate = showRegenerate;
    this.showSave = showSave;
    this.defaultSortingDirection = defaultSortingDirection;
    this.isScroll = isScroll;
    this.scrollHeight = scrollHeight;
    this.scrollWidth = scrollWidth;
    this.isAllowDelete = isAllowDelete;
    this.isTitle = isTitle;
    this.isAllowEdit = isAllowEdit;
    this.deleteMultipleAPI = deleteMultipleAPI;
    this.deleteSelPopupTitle = deleteSelPopupTitle;
    this.deleteSelPopupSubTitle = deleteSelPopupSubTitle;
    this.deleteSelPopupButtonText = deleteSelPopupButtonText;
  }
}

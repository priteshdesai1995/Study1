import { CONFIGCONSTANTS } from "../_constant/app.constant";

export class DataTableColumn {
  title: string;
  key: string;
  isImage: boolean = false;
  defaultValue: string;
  sortable: boolean = true;
  postFixCharacter : string = null;
  highlightOnChecked: boolean = false;
  expandKey = null;
  template: boolean = false;
  width: string = '';

  constructor(
    title: string,
    key: string,
    isImage: boolean = false,
    sortable: boolean = true,
    defaultValue: string = CONFIGCONSTANTS.listTableDefaultValue,
    postFixCharacter : string = null,
    highlightOnChecked: boolean = false,
    expandKey = null,
    template: boolean = false,
    width: string = ''
  ) {
    this.title = title;
    this.key = key;
    this.isImage = isImage;
    this.sortable = sortable;
    this.defaultValue = defaultValue;
    this.postFixCharacter = postFixCharacter;
    this.highlightOnChecked = highlightOnChecked;
    this.expandKey = expandKey;
    this.template = template;
    this.width = width;
  }
}

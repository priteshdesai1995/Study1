export class ProductIntelligenceListRequest {
    page: number;
    size: number;
    sort: Sort;
    filter: String;
    constructor(page, size, field,filter: String, desc = false) {
        this.page = page;
        this.size = size;
        this.sort =  new Sort(field, desc == true ? SortOrder.DESC: SortOrder.ASC)
        this.filter=filter;
    }
}

export class Sort {
    field: string;
    order: SortOrder;
    constructor(field, order) {
        this.field = field;
        this.order = order;
    }
}

export enum SortOrder {
    ASC = "ASC",
    DESC="DESC"
}
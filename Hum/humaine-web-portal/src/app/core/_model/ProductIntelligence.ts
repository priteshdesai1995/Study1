export class ProductIntelligence {
    products: ProductInfo[];
    totalCount: number;
    constructor(products:ProductInfo[], totalCount: number) {
        this.products = products;
        this.totalCount = totalCount;
    }
}
export interface ProductInfo {
    productId:    string;
    name:         string;
    productImage: string;
    count: {[key: string]: number}
}


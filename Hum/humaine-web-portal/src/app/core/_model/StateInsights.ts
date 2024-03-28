export class StateInsight {

    popularProducts: ProductInfo[];
    leastPopularProducts: ProductInfo[];
    monthlyPercentage: number;
    todayPercentage: number;
    monthlyProductDataByState: ProductDate[];
}

export interface ProductInfo {
    productId: number;
    productImage: string;
    totalSoldAmount: number;
    totalSoldQuantities: number;
    name: string;
}

export interface ProductDate {
    totalSoldQuantities: number;
    totalCustomers: number;
    totalPurchases: number;
    saleRevenue: number;
}
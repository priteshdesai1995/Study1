
export class IDashboard {
    popularProducts: PopularProduct[] = [];
    leastPopularProducts: LeastPopularProducts[] = [];
    stateData : StateDatum[] = [];
    leastStateData : LeastStateData[] =[];
    monthlyProductData = new MonthlyProductData();
}

export class MonthlyProductData {
    totalCustomers: number = null;
    totalPurchases: number = null;
    totalSoldQuantities: number = null;
    saleRevenue: number = null;
}

export class PopularProduct {
    name: string = '';
    totalSoldQuantities: number = null;
    productId: string = '';
    totalSoldAmount: number = null;
    productImage:string = null;
}
export class LeastPopularProducts {
    name: string = '';
    totalSoldQuantities: number = null;
    productId: string = '';
    totalSoldAmount: number = null;
    productImage:string = null;

}
export class StateDatum {
    state: string = '';
    totalSoldAmount: number = null;
}

export class LeastStateData {
    state: string = '';
    totalSoldAmount: number = null;

}

export class StateName {
    stateName: string = null;
}
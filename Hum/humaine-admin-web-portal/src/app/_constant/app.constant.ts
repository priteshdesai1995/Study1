export enum SortDirection {
    ASC = 'asc',
    DESC = 'desc'
}

export const CONFIGCONSTANTS = {
    iv: "ssdkF$HUy2A#D%kd",
    secret_key: "weJiSEvR5yAC5ftB",
    emptyValue: "N/A",
    sideMenus: [
        {
            menu_name: "Home",
            link: '/home',
            icon: '/assets/icon/home-line.png'
        },
        {
            menu_name: "Customers",
            link: '/customers',
            icon: '/assets/icon/base-station-line.png'
        }
    ],
    sortByList: [
        {
            name: "Big Five", value: 'test'
        },
        {
            name: "Group Name", value: 'test'
        },
        {
            name: "Success Match", value: 'test'
        }
    ],
    listTableDefaultValue: '',
    majorityConsumer: '',
    trackingProvider: [
        { key: 'Cookies', value: 'Cookies', checked: false, disabled: false },
    ],
    trackingFlag: [
        { key: 'Yes', value: true, checked: true },
        { key: 'No', value: false, checked: false }
    ],
    industryList: [
        { key: 'Fashion, Cloathing and Accessorries', value: 'Fashion, Cloathing and Accessorries', checked: false },
        { key: 'Health and Beauty', value: 'Health and Beauty', checked: false },
        { key: 'Toys and Baby Equipment', value: 'Toys and Baby Equipment', checked: false },
        { key: 'Books and Physical Media', value: 'Books and Physical Media', checked: false },
        { key: 'Groceries, Food and Drink', value: 'Groceries, Food and Drink', checked: false },
        { key: 'Home and Furniture', value: 'Home and Furniture', checked: false },
        { key: 'Technology including Phones and Computer', value: 'Technology including Phones and Computer', checked: false },
        { key: 'Flower and Gifts', value: 'Flower and Gifts', checked: false }
    ],
    eshop: [
        { key: 'Shopify', value: 'Shopify' },
        { key: 'Big Commerce', value: 'Big Commerce' },
        { key: 'Volusion', value: 'Volusion' },
        { key: 'X-Cart Cloud', value: 'X-Cart Cloud' },
        { key: 'Big Cartel', value: 'Big Cartel' },
        { key: '3d Cart', value: '3d Cart' },
        { key: 'Pinnacle Cart', value: 'Pinnacle Cart' },
        { key: 'Yahoo! Ecommerce', value: 'Yahoo! Ecommerce' },
        { key: 'Wix Online Store Builder', value: 'Wix Online Store Builder' },
        { key: 'Storenvy', value: 'Storenvy' },
        { key: 'Magento', value: 'Magento' },
        { key: 'Other', value: 'Other' },
    ],
    currentAnalytics: [
        { key: 'Google Analytics', value: 'Google Analytics' },
        { key: 'Matoma', value: 'Matoma' },
        { key: 'Kissmetrics', value: 'Kissmetrics' },
        { key: 'Hotjra', value: 'Hotjar' },
        { key: 'Woopra', value: 'Woopra' },
        { key: 'Klaviyo', value: 'Klaviyo' },
        { key: 'Visual Web Optimizer', value: 'Visual Web Optimizer' },
        { key: 'Glew.io', value: 'Glew.io' },
        { key: 'Supermetrics', value: 'Supermetrics' },
        { key: 'Optimizely', value: 'Optimizely' },
        { key: 'Other', value: 'Other' }
    ],
    employee: [{ key: 'Microentreprises: 1 to 9 employees.', value: 'Microentreprises: 1 to 9 employees.' },
    { key: 'Small enterprises: 10 to 49 employees.', value: 'Small enterprises: 10 to 49 employees.' },
    { key: 'Medium-sized enterprises: 50 to 249 employees.', value: 'Medium-sized enterprises: 50 to 249 employees.' },
    { key: 'Large enterprises: 250 employees or more', value: 'Large enterprises: 250 employees or more' },
    ],
    products: [
        { key: '1-10', value: '1-10' },
        { key: '10-25', value: '10-25' },
        { key: '25-50', value: '25-50' },
        { key: '50-100', value: '50-100' },
        { key: '100+', value: '100+' },
      ],
    businessData: [
        { key: 'Basic Data (A contact’s name, email address, phone number, job title..)', value: 'Basic Data' },
        { key: 'Demographic data (Such as gender and income)', value: 'Demographic data' },
        { key: 'Interaction and Behavioral Data', value: 'Interaction and Behavioral Data' },
        { key: 'Other', value: 'Other' }
    ],
    customerControls: ['username', 'email', 'name', 'phone', 'address', 'city', 'state', 'other', 'industry', 'url','eshopHosted','headQuarted', 'consumers',
        'employee', 'productPrice', 'product', 'analytic', 'homePageUrl', 'isUserDataTrack', 'provider', 'trackerDataType',
        'expectationComments'
    ],
    trackingEvents: [
        "Session Start/End",
        "Page Navigation",
        "Product View/Buy",
        "Add/Remove Cart",
        "Add/Remove Wishlist",
        "Product Review/Rate",
        "Discover",
        "Subscribe to newsletter",
        "Search activity",
        "Menu Event",
        "Back Navigation",
        "Save for later",
        "Product Return request",
        "Blog Post Visits",
        "Social Media link",
        "Delete saved product",
        "Text Selection by mouse",
        "product image view",
        "Double Click Event",
        "New Tab Event",
    ],
    
}

export enum RouteURLIdentity {
    CUSTOMERS = "CUSTOMERS",
    AI_GENERATED_JOURNEY = "AI_GENERATED_JOURNEY",
    JOURNEY_ANAYSIS = "JOURNEY_ANALYSIS",
    AI_GENERATED_USER_GROUP = "AI_GENERATED_USER_GROUP"
}

export enum ErrorCodes {
    UNAUTHORIZED = '09',
    SC_FORBIDDEN = '403'
}


import { ProductIntelligenceComponent } from './../../pages/product-intelligence/product-intelligence.component';
import { MenuLinks } from './../_enums/MenuLinks';
import { PersonalisedPlansComponent } from './../../pages/user-registration/personalised-plans/personalised-plans.component';
import { environment } from '../../../environments/environment';

export enum graphAPIparam {
    TWENTY_FOUR_HOURS = 'TWENTY_FOUR_HOURS',
    FORTY_EIGHT_HOURS = 'FORTY_EIGHT_HOURS',
    ONE_WEEK = "ONE_WEEK"
}

export enum BigFive {
    BIG_FIVE = "bigFive",
    GROUP_NAME = "name",
    SUCCESS_MATCH = "successMatch",
    VALUES = "values",
    PERSUASIVE_STRATERGY = "persuasiveStratergies",
    JOURNEY_STEPS = "journeySteps",
    JOURNEY_TIME = "journeyTime",
    JOURNEY_SUCCESS = "successRate",
    USERS = "userCount",
    NOOFUSER = "noOfUser"
}

export enum ChartType {
    INCREASE = "INCREASE",
    DECREASE = "DECREASE"
}

export enum RouteURLIdentity {
    TEST_JOURNEY = "TEST_JOURNEY",
    AI_GENERATED_JOURNEY = "AI_GENERATED_JOURNEY",
    JOURNEY_ANAYSIS = "JOURNEY_ANALYSIS",
    AI_GENERATED_USER_GROUP = "AI_GENERATED_USER_GROUP"
}

export enum UserGroupModuleType {
    CREATE_USER_GROUP = "CREATE_USER_GROUP",
    VIEW_USER_GROUP = "VIEW_USER_GROUP"
}

export enum ErrorCodes {
    UNAUTHORIZED = '09'
}
export enum SortDirection {
    ASC = 'asc',
    DESC = 'desc'
}
export enum MyUserViewOption {
    LIST = 'LIST',
    BUBBLE = 'BUBBLE'
}
export const CONFIGCONSTANTS = {
    dateFormat: 'MMM D, y, h:mm A',
    chartDataInterval: 10000,
    dateShortFormat: 'MMM D, y',
    momentDateTime24Format: 'MM/DD/YYYY hh:mm:ss',
    momentDateTime12Format: 'MM/DD/YYYY h:mm A',
    momentTime24Format: 'hh:mm:ss',
    momentTime12Format: 'hh:mm A',
    momentDateFormat: 'MM/DD/YYYY',
    dobFormat: 'MM/dd/yyyy',
    iv: "ssdkF$HUy2A#D%kd",
    secret_key: "weJiSEvR5yAC5ftB",
    avatarImgBaseUrl: '/assets/avatar/',
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
    employee: [
        { key: 'Microentreprises: 1 to 9 employees.', value: 'Microentreprises: 1 to 9 employees.' },
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
    majorityConsumer: [
        { key: '01', value: '01' },
        { key: '02', value: '02' },
        { key: '03', value: '03' },
        { key: '04', value: '04' },
        { key: '05', value: '05' }
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
    trackingProvider: [
        { key: 'Cookies', value: 'Cookies', checked: false, disabled: false },
    ],
    trackingFlag: [
        { key: 'Yes', value: true, checked: true },
        { key: 'No', value: false, checked: false }
    ],
    gender: [
        { key: 'Male', value: "male", checked: false },
        { key: 'Female', value: "female", checked: false },
        { key: 'Other', value: "other", checked: false }
    ],
    factorExternal: [
        { key: 'Yes', value: false, checked: false },
        { key: 'No', value: false, checked: false },
    ],

    registrationSteps: {
        SIGNUP: 0,
        BUSINESS: 1,
        PERSONALIZED_PAGE: 2
    },
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
    sideMenus: [
        {
            menu_name: "Home",
            link: '/home',
            icon: '/assets/icon/home-line.png'
        },
        {
            menu_name: "Live",
            link: '/live',
            icon: '/assets/icon/base-station-line.png'
        },
        {
            menu_name: "Customer Journey",
            link: '/customer-journey',
            icon: '/assets/icon/map-pin-user-line.png',
            sub_menus: [
                {
                    sub_menu_name: "USERS",
                    active: false
                },
                {
                    sub_menu_name: "Create User Groups",
                    link: '/customer-journey/create-user-groups',
                    active: true
                },
                /*{
                    sub_menu_name: "AI Generated User Groups",
                    link: '/customer-journey/ai-generated-user-groups',
                    active: true
                },*/
                {
                    sub_menu_name: "My User Groups",
                    link: '/customer-journey/my-user-groups',
                    active: true
                },
                {
                    sub_menu_name: "JOURNEYS",
                    active: false
                },
                {
                    sub_menu_name: "Test New Journey",
                    link: '/customer-journey/test-new-journey',
                    active: true,
                    onClickRouting: true,
                    clickData: {
                        countBasedRedirection: true,
                        createURL: '/customer-journey/test-new-journey/create-test-journey',
                        listURL: '/customer-journey/test-new-journey/test-journey-listing',
                        menuKey: MenuLinks.TEST_NEW_JOURNEY
                    }
                },
                /*{
                    sub_menu_name: "AI Generated Journey",
                    link: '/customer-journey/ai-journey',
                    active: true
                },*/
                {
                    sub_menu_name: "My Journey Analysis",
                    link: '/customer-journey/my-journey-analysis',
                    active: true
                }
            ]
        },
        {
            menu_name: "UX Optimization",
            link: '/ux-optimization',
            icon: '/assets/icon/equalizer-line.png',
            sub_menus: [
                {
                    sub_menu_name: "UX Analytics",
                    link: '/ux-optimization/ux-analytics',
                    active: true
                },
                {
                    sub_menu_name: "Heatmaps",
                    link: '/ux-optimization/heatmaps',
                    active: true
                },
                {
                    sub_menu_name: "UX Layout",
                    link: '/ux-optimization/ux-layout',
                    dynamicVal: true,
                    active: true
                }

            ]
        },
        {
            menu_name: "Product Intelligence",
            link: '/product-intelligence',
            icon: '/assets/icon/ux-optimize.svg',
            sub_menus: [
                {
                    sub_menu_name: "All Product Matches",
                    link: '/product-intelligence/all-produt-matches',
                    active: true
                }
            ]
        },
        {
            menu_name: "Integration",
            link: '/integration',
            icon: '/assets/icon/settings-4-line.png'
        },

    ],
    sortByList: [
        {
            name: "Big Five", value: BigFive.BIG_FIVE
        },
        {
            name: "Group Name", value: BigFive.GROUP_NAME
        },
        {
            name: "Success Match", value: BigFive.SUCCESS_MATCH
        }
    ],
    viewList: [
        {
            name: "List View", value: MyUserViewOption.LIST
        },
        {
            name: "Bubble View", value: MyUserViewOption.BUBBLE
        },
    ],
    sortByNumber: [
        {
            name: "Top 5", value: 5
        }, {
            name: "Top 10", value: 10
        }
    ],
    emptyValue: "N/A",
    listTableDefaultValue: "-",
    elementMasterKeys: {
        1: "firstInterest",
        2: "decison",
        3: "purchaseAddCart",
        4: "purchaseBuy",
        5: "purchaseOwnership",
    },
    elementMasterNotSkippable: [1],
    durationTimeList: [
        {
            id: "one2", labelID: "one-tab2", key: "24hrs", checked: true, value: graphAPIparam.TWENTY_FOUR_HOURS
        },
        {
            id: "two2", labelID: "two-tab2", key: "48hrs", checked: false, value: graphAPIparam.FORTY_EIGHT_HOURS
        },
        {
            id: "three2", labelID: "three-tab2", key: "Weekly", checked: false, value: graphAPIparam.ONE_WEEK
        }
    ],
    apDexSelection: [
        {
            id: "one", labelID: "one-tab", key: "24hrs", checked: true, value: graphAPIparam.TWENTY_FOUR_HOURS
        },
        {
            id: "two", labelID: "two-tab", key: "48hrs", checked: false, value: graphAPIparam.FORTY_EIGHT_HOURS
        }
    ],
    pageLoadSelection: [
        {
            id: "one1", labelID: "one-tab1", key: "24hrs", checked: true, value: graphAPIparam.TWENTY_FOUR_HOURS
        },
        {
            id: "two1", labelID: "two-tab1", key: "48hrs", checked: false, value: graphAPIparam.FORTY_EIGHT_HOURS
        }
    ],
    productFilterBy: [
        {
            key: "All",
            value: ""
        },
        {
            key: "Agreeableness",
            value: "agreeableness"
        },
        {
            key: "Conscientiousness",
            value: "conscientiousness"
        },
        {
            key: "Openness To Experience",
            value: "openness-to-experience"
        },
        {
            key: "Extraversion",
            value: "extraversion"
        },
        {
            key: "Neuroticism",
            value: "neuroticism"
        }
    ],

    DeviceType: [
        {
            id: 'desktop',
            image: '/assets/img/desktop.svg',
            isChecked: true,
            isCheckImage: '/assets/img/desktop-white.svg'

        },
        {
            id: 'tablet',
            image: '/assets/img/tablet.svg',
            isChecked: false,
            isCheckImage: '/assets/img/tablet-white.svg'

        },
        {
            id: 'mobile',
            image: '/assets/img/mobile.svg',
            isChecked: false,
            isCheckImage: '/assets/img/mobile-white.svg'
        }
    ],
    HeatmapInteractionTYpe: [
        {
            name: "Click",
            id: "1",
            isChecked: true
        },
        {
            name: "Hover",
            id: "2",
            isDisabled: false,
            isChecked: false
        }
    ],
    HeatMapZoomData: [
        {
            name: "150%",
            value: 1.5
        },
        {
            name: "125%",
            value: 1.25
        },
        {
            name: "100%",
            value: 1
        },
        {
            name: "75%",
            value: 0.75
        },
        {
            name: "50%",
            value: 0.5
        }
    ],
    ProductIntelligenceGroupConfig: {
        "agreeableness": {
            "title": "Agreeableness",
            "cssClass": "green-bg"
        },
        "conscientiousness": {
            "title": "Conscientiousness",
            "cssClass": "bg-info"
        },
        "openness-to-experience": {
            "title": "Openness To Experience",
            "cssClass": "purple-bg"
        },
        "extraversion": {
            "title": "Extraversion",
            "cssClass": "violet-bg"
        },
        "neuroticism": {
            "title": "Neuroticism",
            "cssClass": "orange-bg"
        }
    },
    CardVisibleNo: 3,
    bigFiveFilter: [
        {
            key: "Agreeableness",
            value: "agreeableness",
            img: '/assets/img/Agreeableness-wireframe.svg'
        },
        {
            key: "Conscientiousness",
            value: "conscientiousness",
            img: '/assets/img/Conscientiousness-wireframe.svg'
        },
        {
            key: "Openness To Experience",
            value: "openness-to-experience",
            img: '/assets/img/Openness-To-Experience-wireframe.svg'
        },
        {
            key: "Extraversion",
            value: "extraversion",
            img: '/assets/img/Extraversion-wireframe.svg'
        },
        {
            key: "Neuroticism",
            value: "neuroticism",
            img: '/assets/img/Neurotics-wireframe.svg'
        }
    ],
    PersonaCardTooltip: {
        "Personality": {
            "value": "The combination of characteristics or qualities that form an individual's distinctive character."
        },
        "Values": {
            "value": "The cognitive representations of desirable, abstract goals. They motivate actions."
        },
        "Goals": {
             "value": "The aim of the user group when browsing on your web page." 
        },
        "Persuasive Stratergy":{
            "value": "The principles to persuade or influence others."
        },
        "Motivation To Buy":{
            "value": "The events and situations that motivate your user group when making a decision"
        },
        "Frustations":{
            "value": "The events and situations that frustrate your user group."
        }

    },
}

export enum browsers {
    SAFARI = "Safari",
    CHROME = "Chrome",
    CHROMIUM = "Chromium",
    EDGE = "Edge",
    IE = "IE",
    FIREFOX = "Firefox",
    OPERA = "Opera",
    OTHER = "Other"
}

export enum UxInsightBar {
    START = "Session Start",
    NAV = "Navigation",
    PRODVIEW = "Product View",
    ADDCART = "Add to Cart",
    REMCART = "Remove from Cart",
    ADDLIST = "Add to Wishlist",
    REMLIST = "Remove from Wishlist",
    BUY = "Buy Product",
    REVIEW = "Review Product",
    RATE = "Rate Product",
    END = "Session End",
    DISCOVER = "Discover",
    NEWSLETTER_SUBSCRIBE = "Subscribe Newsletter",
    SEARCH = "Search",
    MENU = "Menu Navigation",
    BACK_NAV = "Back Navigation",
    SAVE_FOR_LATER = "Save for Later",
    PROD_RETURN = "Product Return Request",
    VISIT_BLOG_POST = "Blog Post Visits",
    VISIT_SOCIAL_MEDIA = "Social Media Visit",
    DELETE = "Delete saved product"

}

export enum PersonaCardTitle {
    personality = 'Personality',
    values = 'Values',
    golas = 'Goals',
    persuasive = 'Persuasive Stratergy',
    motivationToBuy = 'Motivation To Buy',
    frustation = 'Frustations'
}


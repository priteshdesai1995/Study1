export const CONFIGCONSTANTS = {
  siteName: "Base Angular Admin",
  dateFormat: "MM/dd/yyyy hh:mm:ss",
  momentDateTime24Format: "DD/MM/YYYY",
  momentDateTime12Format: "MM/DD/YYYY h:mm A",
  momentTime24Format: "hh:mm:ss",
  momentTime12Format: "hh:mm A",
  momentDateFormat: "MM/DD/YYYY",
  dobFormat: "MM/dd/yyyy",
  languageOption: true,
  dateRangeLocale: {
    format: "MM/DD/YYYY",
    separator: " To ",
    applyLabel: "Apply",
    cancleLabel: "Cancel",
  },
  dateRangeConfig: {
    rangeInputFormat: "MM/DD/YYYY",
    containerClass: "theme-red",
    rangeSeparator: " To ",
  },
  "CK-Editor-config": {
    allowedContent: true,
    extraPlugins: "sourcedialog,uploadimage",
    forcePasteAsPlainText: true,
    removePlugins: "sourcearea",
    language: "en",
    // removeButtons:"Cut,Copy,Paste,PasteText,PasteFromWord,Undo,Redo,Find,Replace,
    // SelectAll,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,
    // HiddenField,Image,Flash,Table,HorizontalRule,Smiley,SpecialChar,PageBreak,Iframe,About",
    uploadUrl:
      "https://ckeditor.com/apps/ckfinder/3.4.5/core/connector/php/connector.php?command=QuickUpload&type=Files&responseType=json",
    // Configure your file manager integration. This example uses CKFinder 3 for PHP.
    // filebrowserBrowseUrl: 'https://ckeditor.com/apps/ckfinder/3.4.5/ckfinder.html',
    // filebrowserImageBrowseUrl: 'https://ckeditor.com/apps/ckfinder/3.4.5/ckfinder.html?type=Images',
    // filebrowserUploadUrl: 'https://ckeditor.com/apps/ckfinder/3.4.5/core/connector/php/connector.php?command=QuickUpload&type=Files',
    filebrowserImageUploadUrl:
      "https://ckeditor.com/apps/ckfinder/3.4.5/core/connector/php/connector.php?command=QuickUpload&type=Images",
    removeDialogTabs: "image:advanced;link:advanced",
  },
  datatableConfig: {
    reorderable: false,
    scrollbarH: true,
    serverSorting: true,
    clientSorting: false,
    serverPaging: true,
    clientPaging: false,
    piningRight: false,
    headerHeight: 50,
    footerHeight: 50,
    rowHeight: "auto",
    dtMessages: {
      emptyMessage: "No data",
      totalMessage: "Total Records",
    },
    page: {
      size: 10,
      pageNumber: 0,
      totalReords: 0,
    },
    limitList: [10, 25, 50, 100],
  },
  statusClass: {
    Pending: "badge-secondary",
    PENDING: "badge-secondary",
    Active: "badge-success",
    ACTIVE: "badge-success",
    Inactive: "badge-danger",
    INACTIVE: "badge-danger",
    Approved: "badge-success",
    Disapproved: "badge-danger",
    Blocked: "badge-danger",
    Accepted: "badge-success",
    Rejected: "badge-danger",
    Announced: "badge-success",
    "In-Progress": "badge-secondary",
    Sent: "badge-success",
    Failed: "badge-danger",
    Yes: "badge-success",
    No: "badge-danger",
    Close: "badge-danger",
    Working: "badge-warning",
  },

  /*
   * 'key' [means disabledArray index] : contains the key that you want to disable
   * 'child_role' : contains the array of keys on which this array_key is
   *                checked or unchecked and it will depend on the boolean parameter
   *                'strict_check'. if we have more then one keys and if we set
   *                strict_check to 'true' then Parent Key would be selected if all it's
   *                key elements are selected together and if we set strict_check to 'false'
   *                then Parent Key would be depended to any one of selection of key elements.
   * 'strict_check' : optional parameter default value false */
  rolePermissionDisabled: {
    CMS_UPDATE: {
      child_role: ["CMS_CREATE", "CMS_DELETE", "CMS_LIST"],
      strict_check: true,
    },
    FAQ_CREATE: {
      child_role: ["FAQ_UPDATE"],
    },
  },
  clientId:
    "648735729667-pad8fqi9od1mml4prip0ulcbdsfcg1o2.apps.googleusercontent.com",
  clientSecret: "RzjUp4o5BGf2l1T7-HSiTuvy",
  calendarScope: "https://www.googleapis.com/auth/calendar",
  suggestionId: 9,
  departmentId: 1,
  locationId: 6,
};

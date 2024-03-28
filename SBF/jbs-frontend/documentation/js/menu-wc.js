'use strict';


customElements.define('compodoc-menu', class extends HTMLElement {
    constructor() {
        super();
        this.isNormalMode = this.getAttribute('mode') === 'normal';
    }

    connectedCallback() {
        this.render(this.isNormalMode);
    }

    render(isNormalMode) {
        let tp = lithtml.html(`
        <nav>
            <ul class="list">
                <li class="title">
                    <a href="index.html" data-type="index-link">@brainvire/basemodule documentation</a>
                </li>

                <li class="divider"></li>
                ${ isNormalMode ? `<div id="book-search-input" role="search"><input type="text" placeholder="Type to search"></div>` : '' }
                <li class="chapter">
                    <a data-type="chapter-link" href="index.html"><span class="icon ion-ios-home"></span>Getting started</a>
                    <ul class="links">
                        <li class="link">
                            <a href="overview.html" data-type="chapter-link">
                                <span class="icon ion-ios-keypad"></span>Overview
                            </a>
                        </li>
                        <li class="link">
                            <a href="index.html" data-type="chapter-link">
                                <span class="icon ion-ios-paper"></span>README
                            </a>
                        </li>
                        <li class="link">
                            <a href="changelog.html"  data-type="chapter-link">
                                <span class="icon ion-ios-paper"></span>CHANGELOG
                            </a>
                        </li>
                        <li class="link">
                            <a href="contributing.html"  data-type="chapter-link">
                                <span class="icon ion-ios-paper"></span>CONTRIBUTING
                            </a>
                        </li>
                        <li class="link">
                            <a href="license.html"  data-type="chapter-link">
                                <span class="icon ion-ios-paper"></span>LICENSE
                            </a>
                        </li>
                                <li class="link">
                                    <a href="dependencies.html" data-type="chapter-link">
                                        <span class="icon ion-ios-list"></span>Dependencies
                                    </a>
                                </li>
                    </ul>
                </li>
                    <li class="chapter modules">
                        <a data-type="chapter-link" href="modules.html">
                            <div class="menu-toggler linked" data-toggle="collapse" ${ isNormalMode ?
                                'data-target="#modules-links"' : 'data-target="#xs-modules-links"' }>
                                <span class="icon ion-ios-archive"></span>
                                <span class="link-name">Modules</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                        </a>
                        <ul class="links collapse " ${ isNormalMode ? 'id="modules-links"' : 'id="xs-modules-links"' }>
                            <li class="link">
                                <a href="modules/ActivityTrackingModule.html" data-type="entity-link">ActivityTrackingModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ActivityTrackingModule-95fc0fd0c16922365ce5899a4f43dd1e"' : 'data-target="#xs-components-links-module-ActivityTrackingModule-95fc0fd0c16922365ce5899a4f43dd1e"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ActivityTrackingModule-95fc0fd0c16922365ce5899a4f43dd1e"' :
                                            'id="xs-components-links-module-ActivityTrackingModule-95fc0fd0c16922365ce5899a4f43dd1e"' }>
                                            <li class="link">
                                                <a href="components/ActivityTrackingComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ActivityTrackingComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ListActivityTrackingComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ListActivityTrackingComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ActivityTrackingRoutingModule.html" data-type="entity-link">ActivityTrackingRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/AdminSuggestionModule.html" data-type="entity-link">AdminSuggestionModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-AdminSuggestionModule-63c1612a5664813a634ce2a462e6c0c2"' : 'data-target="#xs-components-links-module-AdminSuggestionModule-63c1612a5664813a634ce2a462e6c0c2"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-AdminSuggestionModule-63c1612a5664813a634ce2a462e6c0c2"' :
                                            'id="xs-components-links-module-AdminSuggestionModule-63c1612a5664813a634ce2a462e6c0c2"' }>
                                            <li class="link">
                                                <a href="components/AdminSuggestionComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AdminSuggestionComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AdminSuggestionListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AdminSuggestionListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/AdminSuggestionRoutingModule.html" data-type="entity-link">AdminSuggestionRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/AnnouncementModule.html" data-type="entity-link">AnnouncementModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-AnnouncementModule-7d11c004ff247ae430e08766a00f72fa"' : 'data-target="#xs-components-links-module-AnnouncementModule-7d11c004ff247ae430e08766a00f72fa"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-AnnouncementModule-7d11c004ff247ae430e08766a00f72fa"' :
                                            'id="xs-components-links-module-AnnouncementModule-7d11c004ff247ae430e08766a00f72fa"' }>
                                            <li class="link">
                                                <a href="components/AnnouncementAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AnnouncementAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AnnouncementComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AnnouncementComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AnnouncementDetailsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AnnouncementDetailsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AnnouncementListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AnnouncementListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AnnouncementReceiversComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AnnouncementReceiversComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/AnnouncementRoutingModule.html" data-type="entity-link">AnnouncementRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/AppModule.html" data-type="entity-link">AppModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-AppModule-0bfad0696a3c55c515974a687f8b71ce"' : 'data-target="#xs-components-links-module-AppModule-0bfad0696a3c55c515974a687f8b71ce"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-AppModule-0bfad0696a3c55c515974a687f8b71ce"' :
                                            'id="xs-components-links-module-AppModule-0bfad0696a3c55c515974a687f8b71ce"' }>
                                            <li class="link">
                                                <a href="components/AppComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AppComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CheckUserListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CheckUserListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DefaultLayoutComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">DefaultLayoutComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/LoginComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">LoginComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/OfferReportListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">OfferReportListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/P403Component.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">P403Component</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/P404Component.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">P404Component</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/P500Component.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">P500Component</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/PopupImageOpenComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">PopupImageOpenComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/RegisterComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">RegisterComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ResetPasswordComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ResetPasswordComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SurveyUserReportComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SurveyUserReportComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TextUserListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">TextUserListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/AppRoutingModule.html" data-type="entity-link">AppRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/BaseModule.html" data-type="entity-link">BaseModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-BaseModule-5e9c6664b43be052acd0688c0e01b359"' : 'data-target="#xs-components-links-module-BaseModule-5e9c6664b43be052acd0688c0e01b359"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-BaseModule-5e9c6664b43be052acd0688c0e01b359"' :
                                            'id="xs-components-links-module-BaseModule-5e9c6664b43be052acd0688c0e01b359"' }>
                                            <li class="link">
                                                <a href="components/CardsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CardsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CarouselsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CarouselsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CollapsesComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CollapsesComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FormsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">FormsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/PaginationsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">PaginationsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/PopoversComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">PopoversComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ProgressComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ProgressComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SwitchesComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SwitchesComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TablesComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">TablesComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TabsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">TabsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TooltipsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">TooltipsComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/BaseRoutingModule.html" data-type="entity-link">BaseRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/BsMediaModule.html" data-type="entity-link">BsMediaModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-BsMediaModule-aac9784ff3344ccb03e5efda28ba4ff0"' : 'data-target="#xs-components-links-module-BsMediaModule-aac9784ff3344ccb03e5efda28ba4ff0"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-BsMediaModule-aac9784ff3344ccb03e5efda28ba4ff0"' :
                                            'id="xs-components-links-module-BsMediaModule-aac9784ff3344ccb03e5efda28ba4ff0"' }>
                                            <li class="link">
                                                <a href="components/BsMediaComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">BsMediaComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BsMediaListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">BsMediaListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BsMediaListDetailsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">BsMediaListDetailsComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/BsMediaRoutingModule.html" data-type="entity-link">BsMediaRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ButtonsModule.html" data-type="entity-link">ButtonsModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ButtonsModule-eab2f0b09f18ac6eed9b2bc5334deb5c"' : 'data-target="#xs-components-links-module-ButtonsModule-eab2f0b09f18ac6eed9b2bc5334deb5c"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ButtonsModule-eab2f0b09f18ac6eed9b2bc5334deb5c"' :
                                            'id="xs-components-links-module-ButtonsModule-eab2f0b09f18ac6eed9b2bc5334deb5c"' }>
                                            <li class="link">
                                                <a href="components/BrandButtonsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">BrandButtonsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ButtonsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ButtonsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DropdownsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">DropdownsComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ButtonsRoutingModule.html" data-type="entity-link">ButtonsRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/CategoryModule.html" data-type="entity-link">CategoryModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-CategoryModule-88fca54f45f684f6f9ff2ee4b74b04c6"' : 'data-target="#xs-components-links-module-CategoryModule-88fca54f45f684f6f9ff2ee4b74b04c6"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-CategoryModule-88fca54f45f684f6f9ff2ee4b74b04c6"' :
                                            'id="xs-components-links-module-CategoryModule-88fca54f45f684f6f9ff2ee4b74b04c6"' }>
                                            <li class="link">
                                                <a href="components/CategoryAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CategoryAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CategoryComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CategoryComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CategoryListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CategoryListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CategoryTreeviewComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CategoryTreeviewComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/CategoryRoutingModule.html" data-type="entity-link">CategoryRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ChartJSModule.html" data-type="entity-link">ChartJSModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ChartJSModule-de343795952e3abc0d97475d791969d3"' : 'data-target="#xs-components-links-module-ChartJSModule-de343795952e3abc0d97475d791969d3"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ChartJSModule-de343795952e3abc0d97475d791969d3"' :
                                            'id="xs-components-links-module-ChartJSModule-de343795952e3abc0d97475d791969d3"' }>
                                            <li class="link">
                                                <a href="components/ChartJSComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ChartJSComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ChartJSRoutingModule.html" data-type="entity-link">ChartJSRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/CmsModule.html" data-type="entity-link">CmsModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-CmsModule-6f914b937d56ee3fe71cd0542c8d793d"' : 'data-target="#xs-components-links-module-CmsModule-6f914b937d56ee3fe71cd0542c8d793d"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-CmsModule-6f914b937d56ee3fe71cd0542c8d793d"' :
                                            'id="xs-components-links-module-CmsModule-6f914b937d56ee3fe71cd0542c8d793d"' }>
                                            <li class="link">
                                                <a href="components/CmsAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CmsAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CmsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CmsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CmsListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CmsListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CmsPageBuilderComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CmsPageBuilderComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CmsPageViewComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CmsPageViewComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#pipes-links-module-CmsModule-6f914b937d56ee3fe71cd0542c8d793d"' : 'data-target="#xs-pipes-links-module-CmsModule-6f914b937d56ee3fe71cd0542c8d793d"' }>
                                            <span class="icon ion-md-add"></span>
                                            <span>Pipes</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="pipes-links-module-CmsModule-6f914b937d56ee3fe71cd0542c8d793d"' :
                                            'id="xs-pipes-links-module-CmsModule-6f914b937d56ee3fe71cd0542c8d793d"' }>
                                            <li class="link">
                                                <a href="pipes/SafeHtml.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SafeHtml</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/CmsRoutingModule.html" data-type="entity-link">CmsRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/DashboardModule.html" data-type="entity-link">DashboardModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-DashboardModule-9bb127f6b7b31b444b1b0a62facb17d8"' : 'data-target="#xs-components-links-module-DashboardModule-9bb127f6b7b31b444b1b0a62facb17d8"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-DashboardModule-9bb127f6b7b31b444b1b0a62facb17d8"' :
                                            'id="xs-components-links-module-DashboardModule-9bb127f6b7b31b444b1b0a62facb17d8"' }>
                                            <li class="link">
                                                <a href="components/DashboardComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">DashboardComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/DashboardRoutingModule.html" data-type="entity-link">DashboardRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/EmailModule.html" data-type="entity-link">EmailModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-EmailModule-a1952d6b824bfcb82f1c2776d271a943"' : 'data-target="#xs-components-links-module-EmailModule-a1952d6b824bfcb82f1c2776d271a943"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-EmailModule-a1952d6b824bfcb82f1c2776d271a943"' :
                                            'id="xs-components-links-module-EmailModule-a1952d6b824bfcb82f1c2776d271a943"' }>
                                            <li class="link">
                                                <a href="components/EmailAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">EmailAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/EmailComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">EmailComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/EmailListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">EmailListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/EmailRoutingModule.html" data-type="entity-link">EmailRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/FaqModule.html" data-type="entity-link">FaqModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-FaqModule-db051f307373d10eeed5a00be8a06792"' : 'data-target="#xs-components-links-module-FaqModule-db051f307373d10eeed5a00be8a06792"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-FaqModule-db051f307373d10eeed5a00be8a06792"' :
                                            'id="xs-components-links-module-FaqModule-db051f307373d10eeed5a00be8a06792"' }>
                                            <li class="link">
                                                <a href="components/FaqAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">FaqAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FaqComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">FaqComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FaqListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">FaqListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/FaqRoutingModule.html" data-type="entity-link">FaqRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/IconsModule.html" data-type="entity-link">IconsModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-IconsModule-44bd0412a126e148e97e5ef08ff6db59"' : 'data-target="#xs-components-links-module-IconsModule-44bd0412a126e148e97e5ef08ff6db59"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-IconsModule-44bd0412a126e148e97e5ef08ff6db59"' :
                                            'id="xs-components-links-module-IconsModule-44bd0412a126e148e97e5ef08ff6db59"' }>
                                            <li class="link">
                                                <a href="components/CoreUIIconsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CoreUIIconsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FlagsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">FlagsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FontAwesomeComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">FontAwesomeComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SimpleLineIconsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SimpleLineIconsComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/IconsRoutingModule.html" data-type="entity-link">IconsRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/LoaderModule.html" data-type="entity-link">LoaderModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-LoaderModule-d905c11bdb3a17f14b5a06cb9239b975"' : 'data-target="#xs-components-links-module-LoaderModule-d905c11bdb3a17f14b5a06cb9239b975"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-LoaderModule-d905c11bdb3a17f14b5a06cb9239b975"' :
                                            'id="xs-components-links-module-LoaderModule-d905c11bdb3a17f14b5a06cb9239b975"' }>
                                            <li class="link">
                                                <a href="components/LoaderComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">LoaderComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageBannerModule.html" data-type="entity-link">ManageBannerModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageBannerModule-815b1b57a745cd08e9a9f43035fd7a12"' : 'data-target="#xs-components-links-module-ManageBannerModule-815b1b57a745cd08e9a9f43035fd7a12"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageBannerModule-815b1b57a745cd08e9a9f43035fd7a12"' :
                                            'id="xs-components-links-module-ManageBannerModule-815b1b57a745cd08e9a9f43035fd7a12"' }>
                                            <li class="link">
                                                <a href="components/ManageBannerAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageBannerAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageBannerComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageBannerComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageBannerListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageBannerListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageBannerRoutingModule.html" data-type="entity-link">ManageBannerRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ManageContactModule.html" data-type="entity-link">ManageContactModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageContactModule-5a5e8890c30e3868ef1328ae212bf2f3"' : 'data-target="#xs-components-links-module-ManageContactModule-5a5e8890c30e3868ef1328ae212bf2f3"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageContactModule-5a5e8890c30e3868ef1328ae212bf2f3"' :
                                            'id="xs-components-links-module-ManageContactModule-5a5e8890c30e3868ef1328ae212bf2f3"' }>
                                            <li class="link">
                                                <a href="components/ManageContactComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageContactComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageContactListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageContactListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageContactRoutingModule.html" data-type="entity-link">ManageContactRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ManageEventModule.html" data-type="entity-link">ManageEventModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageEventModule-7d4bdf6bddd042d9a4b21bbdb3348f40"' : 'data-target="#xs-components-links-module-ManageEventModule-7d4bdf6bddd042d9a4b21bbdb3348f40"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageEventModule-7d4bdf6bddd042d9a4b21bbdb3348f40"' :
                                            'id="xs-components-links-module-ManageEventModule-7d4bdf6bddd042d9a4b21bbdb3348f40"' }>
                                            <li class="link">
                                                <a href="components/EventAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">EventAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/EventListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">EventListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/EventViewComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">EventViewComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageEventComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageEventComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageEventRoutingModule.html" data-type="entity-link">ManageEventRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ManageLocationModule.html" data-type="entity-link">ManageLocationModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageLocationModule-fa89b2ba3923ca91d13654429bfcf783"' : 'data-target="#xs-components-links-module-ManageLocationModule-fa89b2ba3923ca91d13654429bfcf783"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageLocationModule-fa89b2ba3923ca91d13654429bfcf783"' :
                                            'id="xs-components-links-module-ManageLocationModule-fa89b2ba3923ca91d13654429bfcf783"' }>
                                            <li class="link">
                                                <a href="components/CityAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CityAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CountryAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CountryAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageCityComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageCityComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageCountryComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageCountryComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageLocationComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageLocationComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageStateComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageStateComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/StateAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">StateAddEditComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageLocationRoutingModule.html" data-type="entity-link">ManageLocationRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ManageOfferModule.html" data-type="entity-link">ManageOfferModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageOfferModule-bc6f2a7410fdfc71343ddeb4289e92ce"' : 'data-target="#xs-components-links-module-ManageOfferModule-bc6f2a7410fdfc71343ddeb4289e92ce"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageOfferModule-bc6f2a7410fdfc71343ddeb4289e92ce"' :
                                            'id="xs-components-links-module-ManageOfferModule-bc6f2a7410fdfc71343ddeb4289e92ce"' }>
                                            <li class="link">
                                                <a href="components/ManageOfferAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageOfferAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageOfferComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageOfferComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageOfferListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageOfferListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageOfferRoutingModule.html" data-type="entity-link">ManageOfferRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ManageRulesetModule.html" data-type="entity-link">ManageRulesetModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageRulesetModule-b55e6c887548ba0c31a3a641fd2f6da1"' : 'data-target="#xs-components-links-module-ManageRulesetModule-b55e6c887548ba0c31a3a641fd2f6da1"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageRulesetModule-b55e6c887548ba0c31a3a641fd2f6da1"' :
                                            'id="xs-components-links-module-ManageRulesetModule-b55e6c887548ba0c31a3a641fd2f6da1"' }>
                                            <li class="link">
                                                <a href="components/ManageRulesetComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageRulesetComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/RulesetAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">RulesetAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/RulesetListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">RulesetListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageRulesetRoutingModule.html" data-type="entity-link">ManageRulesetRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ManageSubscriptionModule.html" data-type="entity-link">ManageSubscriptionModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageSubscriptionModule-b64cab77a6a8797eeea2a942ca921de9"' : 'data-target="#xs-components-links-module-ManageSubscriptionModule-b64cab77a6a8797eeea2a942ca921de9"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageSubscriptionModule-b64cab77a6a8797eeea2a942ca921de9"' :
                                            'id="xs-components-links-module-ManageSubscriptionModule-b64cab77a6a8797eeea2a942ca921de9"' }>
                                            <li class="link">
                                                <a href="components/ManageSubscriptionComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageSubscriptionComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SubscriptionAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SubscriptionAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SubscriptionListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SubscriptionListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SubscriptionViewComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SubscriptionViewComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageSubscriptionRoutingModule.html" data-type="entity-link">ManageSubscriptionRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ManageSurveyModule.html" data-type="entity-link">ManageSurveyModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageSurveyModule-68173bfc4b3bc552ebde19a3db738e92"' : 'data-target="#xs-components-links-module-ManageSurveyModule-68173bfc4b3bc552ebde19a3db738e92"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageSurveyModule-68173bfc4b3bc552ebde19a3db738e92"' :
                                            'id="xs-components-links-module-ManageSurveyModule-68173bfc4b3bc552ebde19a3db738e92"' }>
                                            <li class="link">
                                                <a href="components/ManageSurveyComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageSurveyComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SurveyAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SurveyAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SurveyListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SurveyListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SurveyQuestionsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SurveyQuestionsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SurveyReportComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SurveyReportComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageSurveyRoutingModule.html" data-type="entity-link">ManageSurveyRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ManageUserModule.html" data-type="entity-link">ManageUserModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ManageUserModule-dfcc4cb362f6fc8396270b668937e94d"' : 'data-target="#xs-components-links-module-ManageUserModule-dfcc4cb362f6fc8396270b668937e94d"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ManageUserModule-dfcc4cb362f6fc8396270b668937e94d"' :
                                            'id="xs-components-links-module-ManageUserModule-dfcc4cb362f6fc8396270b668937e94d"' }>
                                            <li class="link">
                                                <a href="components/ManageUserAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageUserAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageUserComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageUserComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageUserListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageUserListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ManageUserViewComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ManageUserViewComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ManageUserRoutingModule.html" data-type="entity-link">ManageUserRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/NotificationsModule.html" data-type="entity-link">NotificationsModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-NotificationsModule-883b3d46dd995e0acce81a8d74eb3dc0"' : 'data-target="#xs-components-links-module-NotificationsModule-883b3d46dd995e0acce81a8d74eb3dc0"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-NotificationsModule-883b3d46dd995e0acce81a8d74eb3dc0"' :
                                            'id="xs-components-links-module-NotificationsModule-883b3d46dd995e0acce81a8d74eb3dc0"' }>
                                            <li class="link">
                                                <a href="components/AlertsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AlertsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BadgesComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">BadgesComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ModalsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ModalsComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/NotificationsRoutingModule.html" data-type="entity-link">NotificationsRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ProfileModule.html" data-type="entity-link">ProfileModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ProfileModule-15f0bfab7604f03b919dd06b26eaa62e"' : 'data-target="#xs-components-links-module-ProfileModule-15f0bfab7604f03b919dd06b26eaa62e"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ProfileModule-15f0bfab7604f03b919dd06b26eaa62e"' :
                                            'id="xs-components-links-module-ProfileModule-15f0bfab7604f03b919dd06b26eaa62e"' }>
                                            <li class="link">
                                                <a href="components/ProfileComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ProfileComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ProfileRoutingModule.html" data-type="entity-link">ProfileRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ReviewModule.html" data-type="entity-link">ReviewModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ReviewModule-6394ff1f11019e4506da5e816ab98511"' : 'data-target="#xs-components-links-module-ReviewModule-6394ff1f11019e4506da5e816ab98511"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ReviewModule-6394ff1f11019e4506da5e816ab98511"' :
                                            'id="xs-components-links-module-ReviewModule-6394ff1f11019e4506da5e816ab98511"' }>
                                            <li class="link">
                                                <a href="components/ReviewComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ReviewComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ReviewRoutingModule.html" data-type="entity-link">ReviewRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/RolePermissionModule.html" data-type="entity-link">RolePermissionModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-RolePermissionModule-306cf84fc51c2befea68c4afc47068df"' : 'data-target="#xs-components-links-module-RolePermissionModule-306cf84fc51c2befea68c4afc47068df"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-RolePermissionModule-306cf84fc51c2befea68c4afc47068df"' :
                                            'id="xs-components-links-module-RolePermissionModule-306cf84fc51c2befea68c4afc47068df"' }>
                                            <li class="link">
                                                <a href="components/RolePermissionComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">RolePermissionComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/RolePermissionRoutingModule.html" data-type="entity-link">RolePermissionRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/SettingsModule.html" data-type="entity-link">SettingsModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-SettingsModule-02d9df940a94e9a1f4c323b952cce6da"' : 'data-target="#xs-components-links-module-SettingsModule-02d9df940a94e9a1f4c323b952cce6da"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-SettingsModule-02d9df940a94e9a1f4c323b952cce6da"' :
                                            'id="xs-components-links-module-SettingsModule-02d9df940a94e9a1f4c323b952cce6da"' }>
                                            <li class="link">
                                                <a href="components/SettingsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SettingsComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/SettingsRoutingModule.html" data-type="entity-link">SettingsRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/SharedModule.html" data-type="entity-link">SharedModule</a>
                                <li class="chapter inner">
                                    <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                        'data-target="#directives-links-module-SharedModule-ca65718dc11c4128f277bb66e8c48931"' : 'data-target="#xs-directives-links-module-SharedModule-ca65718dc11c4128f277bb66e8c48931"' }>
                                        <span class="icon ion-md-code-working"></span>
                                        <span>Directives</span>
                                        <span class="icon ion-ios-arrow-down"></span>
                                    </div>
                                    <ul class="links collapse" ${ isNormalMode ? 'id="directives-links-module-SharedModule-ca65718dc11c4128f277bb66e8c48931"' :
                                        'id="xs-directives-links-module-SharedModule-ca65718dc11c4128f277bb66e8c48931"' }>
                                        <li class="link">
                                            <a href="directives/NumberOnlyDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">NumberOnlyDirective</a>
                                        </li>
                                        <li class="link">
                                            <a href="directives/PhoneMaskDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">PhoneMaskDirective</a>
                                        </li>
                                    </ul>
                                </li>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#pipes-links-module-SharedModule-ca65718dc11c4128f277bb66e8c48931"' : 'data-target="#xs-pipes-links-module-SharedModule-ca65718dc11c4128f277bb66e8c48931"' }>
                                            <span class="icon ion-md-add"></span>
                                            <span>Pipes</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="pipes-links-module-SharedModule-ca65718dc11c4128f277bb66e8c48931"' :
                                            'id="xs-pipes-links-module-SharedModule-ca65718dc11c4128f277bb66e8c48931"' }>
                                            <li class="link">
                                                <a href="pipes/CustomDate.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">CustomDate</a>
                                            </li>
                                            <li class="link">
                                                <a href="pipes/FormatCkEditorObjectPipe.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">FormatCkEditorObjectPipe</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/SubadminModule.html" data-type="entity-link">SubadminModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-SubadminModule-ab66ccf4bc32efb1b9183ba5d81c7a94"' : 'data-target="#xs-components-links-module-SubadminModule-ab66ccf4bc32efb1b9183ba5d81c7a94"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-SubadminModule-ab66ccf4bc32efb1b9183ba5d81c7a94"' :
                                            'id="xs-components-links-module-SubadminModule-ab66ccf4bc32efb1b9183ba5d81c7a94"' }>
                                            <li class="link">
                                                <a href="components/SubadminAddEditComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SubadminAddEditComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SubadminComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SubadminComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/SubadminListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SubadminListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/SubadminRoutingModule.html" data-type="entity-link">SubadminRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/ThemeModule.html" data-type="entity-link">ThemeModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-ThemeModule-43352daa16d2980e013888a43b6971de"' : 'data-target="#xs-components-links-module-ThemeModule-43352daa16d2980e013888a43b6971de"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ThemeModule-43352daa16d2980e013888a43b6971de"' :
                                            'id="xs-components-links-module-ThemeModule-43352daa16d2980e013888a43b6971de"' }>
                                            <li class="link">
                                                <a href="components/ColorsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ColorsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TypographyComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">TypographyComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ThemeRoutingModule.html" data-type="entity-link">ThemeRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/UserReportModule.html" data-type="entity-link">UserReportModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-UserReportModule-4c369634ee85f0670406dcf74adcee48"' : 'data-target="#xs-components-links-module-UserReportModule-4c369634ee85f0670406dcf74adcee48"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-UserReportModule-4c369634ee85f0670406dcf74adcee48"' :
                                            'id="xs-components-links-module-UserReportModule-4c369634ee85f0670406dcf74adcee48"' }>
                                            <li class="link">
                                                <a href="components/UserReportComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">UserReportComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/UserReportRoutingModule.html" data-type="entity-link">UserReportRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/UserSuggestionModule.html" data-type="entity-link">UserSuggestionModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-UserSuggestionModule-b4109f3a29c6e946f3015126af2c3392"' : 'data-target="#xs-components-links-module-UserSuggestionModule-b4109f3a29c6e946f3015126af2c3392"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-UserSuggestionModule-b4109f3a29c6e946f3015126af2c3392"' :
                                            'id="xs-components-links-module-UserSuggestionModule-b4109f3a29c6e946f3015126af2c3392"' }>
                                            <li class="link">
                                                <a href="components/UserSuggestionAddComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">UserSuggestionAddComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/UserSuggestionComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">UserSuggestionComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/UserSuggestionListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">UserSuggestionListComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/UserSuggestionRoutingModule.html" data-type="entity-link">UserSuggestionRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/UserSurveyModule.html" data-type="entity-link">UserSurveyModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-UserSurveyModule-918c217bebaebbcf56d812647b47ee31"' : 'data-target="#xs-components-links-module-UserSurveyModule-918c217bebaebbcf56d812647b47ee31"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-UserSurveyModule-918c217bebaebbcf56d812647b47ee31"' :
                                            'id="xs-components-links-module-UserSurveyModule-918c217bebaebbcf56d812647b47ee31"' }>
                                            <li class="link">
                                                <a href="components/SurveyAnswersComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">SurveyAnswersComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/UserSurveyComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">UserSurveyComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/UserSurveyListComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">UserSurveyListComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ViewAnswerComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ViewAnswerComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                                <li class="chapter inner">
                                    <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                        'data-target="#directives-links-module-UserSurveyModule-918c217bebaebbcf56d812647b47ee31"' : 'data-target="#xs-directives-links-module-UserSurveyModule-918c217bebaebbcf56d812647b47ee31"' }>
                                        <span class="icon ion-md-code-working"></span>
                                        <span>Directives</span>
                                        <span class="icon ion-ios-arrow-down"></span>
                                    </div>
                                    <ul class="links collapse" ${ isNormalMode ? 'id="directives-links-module-UserSurveyModule-918c217bebaebbcf56d812647b47ee31"' :
                                        'id="xs-directives-links-module-UserSurveyModule-918c217bebaebbcf56d812647b47ee31"' }>
                                        <li class="link">
                                            <a href="directives/MultiCheckboxValidationDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">MultiCheckboxValidationDirective</a>
                                        </li>
                                    </ul>
                                </li>
                            </li>
                            <li class="link">
                                <a href="modules/UserSurveyRoutingModule.html" data-type="entity-link">UserSurveyRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/VideoUploadModule.html" data-type="entity-link">VideoUploadModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-VideoUploadModule-92bee752906408d3e0a8e61c60a1dfc5"' : 'data-target="#xs-components-links-module-VideoUploadModule-92bee752906408d3e0a8e61c60a1dfc5"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-VideoUploadModule-92bee752906408d3e0a8e61c60a1dfc5"' :
                                            'id="xs-components-links-module-VideoUploadModule-92bee752906408d3e0a8e61c60a1dfc5"' }>
                                            <li class="link">
                                                <a href="components/VideoUploadComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">VideoUploadComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/VideoUploadRoutingModule.html" data-type="entity-link">VideoUploadRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/WidgetsModule.html" data-type="entity-link">WidgetsModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-WidgetsModule-3030ca9df3ec3cd9458d60c1562c6354"' : 'data-target="#xs-components-links-module-WidgetsModule-3030ca9df3ec3cd9458d60c1562c6354"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-WidgetsModule-3030ca9df3ec3cd9458d60c1562c6354"' :
                                            'id="xs-components-links-module-WidgetsModule-3030ca9df3ec3cd9458d60c1562c6354"' }>
                                            <li class="link">
                                                <a href="components/WidgetsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">WidgetsComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/WidgetsRoutingModule.html" data-type="entity-link">WidgetsRoutingModule</a>
                            </li>
                </ul>
                </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#classes-links"' :
                            'data-target="#xs-classes-links"' }>
                            <span class="icon ion-ios-paper"></span>
                            <span>Classes</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="classes-links"' : 'id="xs-classes-links"' }>
                            <li class="link">
                                <a href="classes/Cms.html" data-type="entity-link">Cms</a>
                            </li>
                            <li class="link">
                                <a href="classes/Email.html" data-type="entity-link">Email</a>
                            </li>
                            <li class="link">
                                <a href="classes/Faq.html" data-type="entity-link">Faq</a>
                            </li>
                            <li class="link">
                                <a href="classes/Manageuser.html" data-type="entity-link">Manageuser</a>
                            </li>
                            <li class="link">
                                <a href="classes/PasswordValidator.html" data-type="entity-link">PasswordValidator</a>
                            </li>
                            <li class="link">
                                <a href="classes/Role.html" data-type="entity-link">Role</a>
                            </li>
                            <li class="link">
                                <a href="classes/Settings.html" data-type="entity-link">Settings</a>
                            </li>
                            <li class="link">
                                <a href="classes/Subadmin.html" data-type="entity-link">Subadmin</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#injectables-links"' :
                                'data-target="#xs-injectables-links"' }>
                                <span class="icon ion-md-arrow-round-down"></span>
                                <span>Injectables</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                            <ul class="links collapse " ${ isNormalMode ? 'id="injectables-links"' : 'id="xs-injectables-links"' }>
                                <li class="link">
                                    <a href="injectables/ActivityTrackingService.html" data-type="entity-link">ActivityTrackingService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/AnnouncementService.html" data-type="entity-link">AnnouncementService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/AuthenticationService.html" data-type="entity-link">AuthenticationService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/BsMediaServiceService.html" data-type="entity-link">BsMediaServiceService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/CategoryService.html" data-type="entity-link">CategoryService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/CmsService.html" data-type="entity-link">CmsService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/EmailService.html" data-type="entity-link">EmailService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/EncrDecrService.html" data-type="entity-link">EncrDecrService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/FaqService.html" data-type="entity-link">FaqService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/FilterStorageService.html" data-type="entity-link">FilterStorageService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/LanguageService.html" data-type="entity-link">LanguageService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/LoaderService.html" data-type="entity-link">LoaderService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/LocationService.html" data-type="entity-link">LocationService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ManageBannerService.html" data-type="entity-link">ManageBannerService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ManagecontactService.html" data-type="entity-link">ManagecontactService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ManageEventService.html" data-type="entity-link">ManageEventService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ManageOfferService.html" data-type="entity-link">ManageOfferService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ManageSubscriptionService.html" data-type="entity-link">ManageSubscriptionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ManageuserService.html" data-type="entity-link">ManageuserService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/MultilingualService.html" data-type="entity-link">MultilingualService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ReportService.html" data-type="entity-link">ReportService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ReviewService.html" data-type="entity-link">ReviewService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/RolePermissionService.html" data-type="entity-link">RolePermissionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/RulesetService.html" data-type="entity-link">RulesetService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/SettingsService.html" data-type="entity-link">SettingsService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/SubadminService.html" data-type="entity-link">SubadminService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/SuggestionService.html" data-type="entity-link">SuggestionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/SurveyService.html" data-type="entity-link">SurveyService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/UserService.html" data-type="entity-link">UserService</a>
                                </li>
                            </ul>
                        </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#interceptors-links"' :
                            'data-target="#xs-interceptors-links"' }>
                            <span class="icon ion-ios-swap"></span>
                            <span>Interceptors</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="interceptors-links"' : 'id="xs-interceptors-links"' }>
                            <li class="link">
                                <a href="interceptors/ErrorInterceptor.html" data-type="entity-link">ErrorInterceptor</a>
                            </li>
                            <li class="link">
                                <a href="interceptors/JwtInterceptor.html" data-type="entity-link">JwtInterceptor</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#guards-links"' :
                            'data-target="#xs-guards-links"' }>
                            <span class="icon ion-ios-lock"></span>
                            <span>Guards</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="guards-links"' : 'id="xs-guards-links"' }>
                            <li class="link">
                                <a href="guards/AuthGuard.html" data-type="entity-link">AuthGuard</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#miscellaneous-links"'
                            : 'data-target="#xs-miscellaneous-links"' }>
                            <span class="icon ion-ios-cube"></span>
                            <span>Miscellaneous</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="miscellaneous-links"' : 'id="xs-miscellaneous-links"' }>
                            <li class="link">
                                <a href="miscellaneous/functions.html" data-type="entity-link">Functions</a>
                            </li>
                            <li class="link">
                                <a href="miscellaneous/variables.html" data-type="entity-link">Variables</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <a data-type="chapter-link" href="routes.html"><span class="icon ion-ios-git-branch"></span>Routes</a>
                        </li>
                    <li class="chapter">
                        <a data-type="chapter-link" href="coverage.html"><span class="icon ion-ios-stats"></span>Documentation coverage</a>
                    </li>
            </ul>
        </nav>
        `);
        this.innerHTML = tp.strings;
    }
});
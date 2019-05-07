webpackJsonp([1],{

/***/ 135:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_dashPage_vue__ = __webpack_require__(136);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_05887602_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_dashPage_vue__ = __webpack_require__(344);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_dashPage_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_05887602_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_dashPage_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 136:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__viewProfile_vue__ = __webpack_require__(137);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__editProfile_vue__ = __webpack_require__(338);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__helperComponents_navbarMain_vue__ = __webpack_require__(41);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__helperComponents_footerMain_vue__ = __webpack_require__(42);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__helperComponents_unauthorisedPromptPage__ = __webpack_require__(43);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//







/* harmony default export */ __webpack_exports__["a"] = ({
    name: "dashPage",
    props: ['profile', 'nationalityOptions', 'travTypeOptions'],
    created() {
        document.title = "TravelEA - Dashboard";
    },
    data: function () {
        return {
            viewProfile: true,
            editProfile: false
        };
    },
    mounted() {},
    methods: {
        togglePage: function (viewPage) {
            if (!viewPage) {
                this.viewProfile = !this.viewProfile;
                this.editProfile = !this.editProfile;
            }
        }
    },
    components: {
        ViewProfile: __WEBPACK_IMPORTED_MODULE_0__viewProfile_vue__["a" /* default */],
        EditProfile: __WEBPACK_IMPORTED_MODULE_1__editProfile_vue__["a" /* default */],
        NavBarMain: __WEBPACK_IMPORTED_MODULE_2__helperComponents_navbarMain_vue__["a" /* default */],
        FooterMain: __WEBPACK_IMPORTED_MODULE_3__helperComponents_footerMain_vue__["a" /* default */],
        UnauthorisedPrompt: __WEBPACK_IMPORTED_MODULE_4__helperComponents_unauthorisedPromptPage__["a" /* default */]
    }
});

/***/ }),

/***/ 137:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_viewProfile_vue__ = __webpack_require__(138);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_55751c00_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_viewProfile_vue__ = __webpack_require__(337);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_viewProfile_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_55751c00_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_viewProfile_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 138:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__trips_yourTrips_vue__ = __webpack_require__(139);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//



/* harmony default export */ __webpack_exports__["a"] = ({
    name: "viewProfile",
    props: ['profile', 'nationalityOptions', 'travTypeOptions'],
    data() {
        return {};
    },
    components: {
        YourTrips: __WEBPACK_IMPORTED_MODULE_0__trips_yourTrips_vue__["a" /* default */]
    },
    mounted() {},
    methods: {}
});

/***/ }),

/***/ 139:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_yourTrips_vue__ = __webpack_require__(140);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_02e5fd0b_hasScoped_true_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_yourTrips_vue__ = __webpack_require__(336);
function injectStyle (ssrContext) {
  __webpack_require__(334)
}
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = injectStyle
/* scopeId */
var __vue_scopeId__ = "data-v-02e5fd0b"
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_yourTrips_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_02e5fd0b_hasScoped_true_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_yourTrips_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 140:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__planATrip_vue__ = __webpack_require__(141);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//



/* harmony default export */ __webpack_exports__["a"] = ({
    name: "YourTrips",
    props: ['profile'],
    data: function () {
        return {
            optionViews: [{ value: 1, text: "1" }, { value: 5, text: "5" }, { value: 10, text: "10" }, {
                value: 15,
                text: "15"
            }],
            perPageUpcoming: 5,
            perPagePast: 5,
            currentPageUpcoming: 1,
            currentPagePast: 1,
            sortBy: 'destinations[0].startDate',
            fields: ['name', {
                key: 'destinations[0].startDate',
                label: 'Start Date'
            }, { key: 'destinations[1].endDate', label: 'End Date' }, 'more_details'],
            subFields: [{ key: 'destination.name', label: "Destination Name" }, { key: 'destination.type.destinationType', label: "Destination Type" }, { key: 'destination.district', label: "Destination District" }, { key: 'destination.latitude', label: "Destination Latitude" }, { key: 'destination.longitude', label: "Destination Longitude" }, { key: 'destination.startDate', label: "In Date" }, { key: 'destination.endDate', label: "Out Date" }],
            pastTrips: [],
            trips: [],
            retrievingTrips: false
        };
    },
    mounted() {
        this.getAllTrips(trips => this.trips = trips);
    },
    computed: {
        rowsUpcoming() {
            return this.trips.length;
        },
        rowsPast() {
            return this.pastTrips.length;
        }
    },
    components: {
        PlanATrip: __WEBPACK_IMPORTED_MODULE_0__planATrip_vue__["a" /* default */]
    },
    methods: {
        getAllTrips(cb) {
            this.retrievingTrips = true;
            let userId = this.profile.id;
            return fetch(`/v1/trips/all?id=` + userId, {
                accept: "application/json"
            }).then(this.checkStatus).then(this.parseJSON).then(cb);
        },
        checkStatus(response) {
            if (response.status >= 200 && response.status < 300) {
                return response;
            }
            const error = new Error(`HTTP Error ${response.statusText}`);
            error.status = response.statusText;
            error.response = response;
            console.log(error); // eslint-disable-line no-console
            throw error;
        },
        parseJSON(response) {
            this.retrievingTrips = false;
            return response.json();
        }
    }
});

/***/ }),

/***/ 141:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_planATrip_vue__ = __webpack_require__(142);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_1360bf3f_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_planATrip_vue__ = __webpack_require__(335);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_planATrip_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_1360bf3f_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_planATrip_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 142:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["a"] = ({
    name: "PlanATrip",
    props: ['destinations'],
    data() {
        return {
            heading: 'Plan a Trip',
            optionViews: [{ value: 1, text: "1" }, { value: 5, text: "5" }, { value: 10, text: "10" }, {
                value: 15,
                text: "15"
            }],
            perPage: 10,
            currentPage: 1,
            tripName: null,
            tripDestination: "",
            inDate: "",
            outDate: "",
            showError: false,
            errorMessage: "",
            successTripAddedAlert: false,
            dismissSecs: 3,
            dismissCountDown: 0,
            rowEdit: null,
            editInDate: null,
            editOutDate: null,
            fields: ['order', { key: 'name' }, { key: 'in_date' }, { key: 'out_date' }, 'actions'],
            subFields: [{ key: 'type' }, { key: 'district' }, { key: 'latitude' }, { key: 'longitude' }, { key: 'country' }],
            tripDestinations: [],
            savingTrip: false,
            letTripSaved: false

        };
    },
    computed: {
        rows() {
            return this.tripDestinations.length;
        }
    },
    methods: {
        checkDestination: function () {
            if (this.tripDestination) {
                let startDate = new Date(this.inDate);
                let endDate = new Date(this.outDate);

                if (startDate > endDate) {
                    this.showError = true;
                    this.errorMessage = "Incorrect date ordering.";
                } else if (this.tripDestinations.length === 0) {
                    this.addDestination();
                } else if (!this.checkSameDestination(this.tripDestination.id)) {
                    this.addDestination();
                } else {
                    this.showDuplicateDestError("add");
                }
            } else {
                this.showError = true;
                this.errorMessage = "No Destination Selected";
            }
        },
        addDestination() {
            this.showError = false;
            this.tripDestinations.push({
                destId: this.tripDestination.id,
                name: this.tripDestination.name,
                type: this.tripDestination.type.destinationType,
                district: this.tripDestination.district,
                latitude: this.tripDestination.latitude,
                longitude: this.tripDestination.longitude,
                country: this.tripDestination.country,
                in_date: this.inDate,
                out_date: this.outDate
            });
            this.resetDestForm();
        },
        deleteDestination: function (row, rowIndex) {
            if (this.tripDestinations.length > 2) {
                if (rowIndex === this.tripDestinations.length - 1) {
                    this.tripDestinations.splice(rowIndex, 1);
                } else if (this.tripDestinations[rowIndex - 1].destId !== this.tripDestinations[rowIndex + 1].destId) {
                    this.tripDestinations.splice(rowIndex, 1);
                } else {
                    this.showDuplicateDestError("delete");
                }
            } else {
                this.tripDestinations.splice(rowIndex, 1);
            }
        },
        populateModal(row) {
            this.rowEdit = row;
            this.editInDate = row.in_date;
            this.editOutDate = row.out_date;
        },
        moveUpCheck(rowIndex) {
            if (this.tripDestinations.length === 2) {
                this.moveUp(rowIndex);
            } else if (rowIndex === 1 && this.tripDestinations[rowIndex - 1].destId === this.tripDestinations[rowIndex + 1].destId) {
                this.showDuplicateDestError("move");
            } else if (rowIndex === 1) {
                this.moveUp(rowIndex);
            } else if (this.tripDestinations[rowIndex - 2].destId === this.tripDestinations[rowIndex].destId) {
                this.showDuplicateDestError("move");
            } else if (rowIndex === this.getDestinationRows() - 1) {
                this.moveUp(rowIndex);
            } else if (this.tripDestinations[rowIndex - 1].destId === this.tripDestinations[rowIndex + 1].destId) {
                this.showDuplicateDestError("move");
            } else {
                this.moveUp(rowIndex);
            }
        },
        moveUp(rowIndex) {
            let upIndex = rowIndex - 1;
            let swapRow = this.tripDestinations[rowIndex];
            this.tripDestinations[rowIndex] = this.tripDestinations[upIndex];
            this.tripDestinations[upIndex] = swapRow;
            this.$refs.tripDestTable.refresh();
        },
        moveDownCheck(rowIndex) {
            if (this.tripDestinations.length === 2) {
                this.moveDown(rowIndex);
            } else if (rowIndex === 0 && this.tripDestinations[rowIndex + 2].destId !== this.tripDestinations[rowIndex].destId) {
                this.moveDown(rowIndex);
            } else if (rowIndex === 0 && this.tripDestinations[rowIndex + 2].destId === this.tripDestinations[rowIndex].destId) {
                this.showDuplicateDestError("move");
            } else if (this.tripDestinations[rowIndex + 1].destId === this.tripDestinations[rowIndex - 1].destId && rowIndex !== 0) {
                this.showDuplicateDestError("move");
            } else if (this.tripDestinations[rowIndex + 1].destId !== this.tripDestinations[rowIndex - 1].destId && rowIndex !== 0) {
                this.moveDown(rowIndex);
            } else if (this.tripDestinations[rowIndex + 2].destId === this.tripDestinations[rowIndex].destId && this.getDestinationRows() > 2) {
                this.showDuplicateDestError("move");
            } else if (rowIndex === this.getDestinationRows() - 2) {
                this.moveDown(rowIndex);
            } else if (rowIndex === 0 && this.getDestinationRows === 2) {
                this.moveDown(rowIndex);
            } else if (this.tripDestinations[rowIndex + 2].destId === this.tripDestinations[rowIndex].destId) {
                this.showDuplicateDestError("move");
            } else {
                this.moveDown(rowIndex);
            }
        },
        moveDown(rowIndex) {
            let upIndex = rowIndex + 1;
            let swapRow = this.tripDestinations[rowIndex];
            this.tripDestinations[rowIndex] = this.tripDestinations[upIndex];
            this.tripDestinations[upIndex] = swapRow;
            this.$refs.tripDestTable.refresh();
        },
        showDuplicateDestError(error) {
            this.showError = true;
            this.errorMessage = "Can't have same destination next to another, please choose another destination to " + error;
        },
        checkSameDestination(destId) {
            let previousDestinationIndex = this.tripDestinations.length - 1;
            if (this.tripDestinations[previousDestinationIndex].destId === destId) {
                return true;
            }
        },
        saveDestination(row, editInDate, editOutDate) {
            row.in_date = editInDate;
            row.out_date = editOutDate;
            this.dismissModal();
        },
        dismissModal() {
            this.$refs['editModal'].hide();
        },
        countDownChanged(dismissCountDown) {
            this.dismissCountDown = dismissCountDown;
        },
        showAlert() {
            this.dismissCountDown = this.dismissSecs;
        },
        resetDestForm() {
            this.tripDestination = "";
            this.inDate = "";
            this.outDate = "";
        },
        submitTrip: function () {
            if (this.tripName === null || this.tripName.length === 0) {
                this.showError = true;
                this.errorMessage = "No Trip Name";
            } else if (this.tripDestinations.length < 2) {
                this.showError = true;
                this.errorMessage = "There must be at least 2 destinations";
            } else {
                this.showError = false;
                let tripDestinationsList = [];
                for (let i = 0; i < this.tripDestinations.length; i++) {
                    if (this.tripDestinations[i].in_date === undefined || this.tripDestinations[i].in_date.length === 0) {
                        this.tripDestinations[i].in_date = null;
                    }
                    if (this.tripDestinations[i].out_date === undefined || this.tripDestinations[i].out_date.length === 0) {
                        this.tripDestinations[i].out_date = null;
                    }
                    tripDestinationsList.push({
                        destination_id: this.tripDestinations[i].destId,
                        start_date: this.tripDestinations[i].in_date,
                        end_date: this.tripDestinations[i].out_date
                    });
                }
                let trip = {
                    trip_name: this.tripName,
                    trip_destinations: tripDestinationsList
                };
                this.saveTrip(trip);
            }
        },
        saveTrip(trip) {
            this.savingTrip = true;
            let self = this;
            fetch('/v1/trip', {
                method: 'POST',
                headers: { 'content-type': 'application/json' },
                body: JSON.stringify(trip)
            }).then(function (response) {
                if (response.ok) {
                    self.savingTrip = false;
                    self.showAlert();
                    self.$emit('tripSaved', true);
                    self.resetDestForm();
                    self.tripName = "";
                    self.tripDestinations = [];
                    return JSON.parse(JSON.stringify(response));
                } else {
                    throw new Error('Something went wrong, try again later.');
                }
            }).catch(error => {
                this.savingTrip = false;
                this.showError = true;
                this.errorMessage = error;
            });
        },
        getDestinationRows() {
            return this.tripDestinations.length;
        }
    }
});

/***/ }),

/***/ 143:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["a"] = ({
    name: "editProfile",
    props: ['profile', 'nationalityOptions', 'travTypeOptions'],
    data: function () {
        return {
            saveProfile: {
                first_name: this.profile.firstName,
                middle_name: this.profile.middleName,
                last_name: this.profile.lastName,
                username: this.profile.username,
                password: "",
                date_of_birth: this.profile.dateOfBirth,
                gender: this.profile.gender,
                nationality: [],
                passport_country: [],
                traveller_type: []
            },
            rePassword: "",
            validEmail: false,
            showError: false,
            genderOptions: [{ value: 'Male', text: 'Male' }, { value: 'Female', text: 'Female' }, { value: 'Other', text: 'Other' }]

        };
    },
    mounted() {},
    computed: {
        fNameValidation() {
            if (this.saveProfile.first_name.length === 0) {
                return null;
            }
            let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
            return nameRegex.test(this.saveProfile.first_name);
        },
        mNameValidation() {
            let nameRegex = new RegExp("^(?=.{0,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
            return nameRegex.test(this.saveProfile.middle_name) || this.saveProfile.middle_name.length === 0;
        },
        lNameValidation() {
            if (this.saveProfile.last_name.length === 0) {
                return null;
            }
            let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
            return nameRegex.test(this.saveProfile.last_name);
        },
        emailValidation() {
            if (this.saveProfile.username.length === 0) {
                return null;
            }
            let emailRegex = new RegExp("^([a-zA-Z0-9]+(@)([a-zA-Z]+((.)[a-zA-Z]+)*))(?=.{3,15})");
            this.checkUsername();
            return emailRegex.test(this.saveProfile.username) && this.validEmail;
        },
        passwordValidation() {
            if (this.saveProfile.password.length === 0) {
                return null;
            }
            let passwordRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{5,15})");
            return passwordRegex.test(this.saveProfile.password);
        },
        rePasswordValidation() {
            if (this.rePassword.length === 0) {
                return null;
            }
            return this.saveProfile.password.length > 0 && this.rePassword === this.saveProfile.password;
        },
        dateOfBirthValidation() {
            if (this.saveProfile.date_of_birth.length === 0) {
                return null;
            }
            return this.saveProfile.date_of_birth.length > 0;
        },
        genderValidation() {
            if (this.saveProfile.gender.length === 0) {
                return null;
            }
            return this.saveProfile.gender.length > 0;
        },
        nationalityValidation() {
            if (this.saveProfile.nationality.length === 0) {
                return false;
            }
            return this.saveProfile.nationality.length > 0;
        },
        passportValidation() {
            if (this.saveProfile.passport_country.length === 0) {
                return null;
            }
            return this.saveProfile.passport_country.length > 0;
        },
        travTypeValidation() {
            if (this.saveProfile.traveller_type.length === 0) {
                return false;
            }
            return this.saveProfile.traveller_type.length > 0;
        }
    },
    methods: {
        checkUsername() {
            let self = this;
            fetch(`/v1/checkUsername`, {
                method: 'POST',
                headers: { 'content-type': 'application/json' },
                body: JSON.stringify({ 'username': this.saveProfile.username })

            }).then(function (response) {
                self.validEmail = response.ok;
            });
        },
        checkSaveProfile() {
            if (this.fNameValidation && this.mNameValidation && this.lNameValidation && this.emailValidation && this.dateOfBirthValidation && this.genderValidation && this.nationalityValidation && this.travTypeValidation) {
                this.submitSaveProfile();
            } else {
                this.showError = true;
            }
        },
        submitSaveProfile() {
            fetch('/v1/profile', {
                method: 'PUT',
                headers: { 'content-type': 'application/json' },
                body: JSON.stringify(this.saveProfile)
            }).then(function (response) {
                location.reload();
                return response.json();
            });
        },
        duplicateNationality(id) {
            for (let i = 0; i < this.profile.nationalities.length; i++) {
                if (this.profile.nationalities[i].id === id) {
                    return true;
                }
            }
        },
        duplicatePassport(id) {
            for (let i = 0; i < this.profile.passports.length; i++) {
                if (this.profile.passports[i].id === id) {
                    return true;
                }
            }
        },
        duplicateTravType(id) {
            for (let i = 0; i < this.profile.travellerTypes.length; i++) {
                if (this.profile.travellerTypes[i].id === id) {
                    return true;
                }
            }
        }
    },
    components: {}
});

/***/ }),

/***/ 144:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__assets_index__ = __webpack_require__(74);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//



/* harmony default export */ __webpack_exports__["a"] = ({
    name: "navbarMain",
    props: ['profile'],
    computed: {
        assets() {
            return __WEBPACK_IMPORTED_MODULE_0__assets_index__["a" /* default */];
        }
    },
    data() {
        return {
            currentPage: '/dash'
        };
    },
    mounted() {
        this.getCurrentPage();
    },
    methods: {
        logout() {
            let self = this;
            fetch(`/v1/logout`, {
                method: 'POST',
                accept: "application/json"
            }).then(this.parseJSON).then(function (response) {
                if (response.ok) {
                    self.$router.push("/");
                    self.$router.go();
                    return response;
                } else {
                    self.$router.push("/dash");
                    return response;
                }
            });
        },
        getCurrentPage() {
            this.currentPage = this.$router.currentRoute.fullPath;
        },
        goToPeople() {
            this.$router.push("/profiles");
        },
        goToTrips() {
            this.$router.push("/trips");
        },
        goToDestinations() {
            this.$router.push("/destinations");
        },
        goToProfile() {
            this.$router.push("/dash");
        }
    }
});

/***/ }),

/***/ 145:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
//
//
//
//

/* harmony default export */ __webpack_exports__["a"] = ({
    name: "footerMain"
});

/***/ }),

/***/ 146:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["a"] = ({
    name: "unauthorisedPromptPage"
});

/***/ }),

/***/ 147:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__searchDestinations_vue__ = __webpack_require__(346);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__addDestinations_vue__ = __webpack_require__(348);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__helperComponents_navbarMain_vue__ = __webpack_require__(41);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__helperComponents_footerMain_vue__ = __webpack_require__(42);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__helperComponents_unauthorisedPromptPage__ = __webpack_require__(43);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//







/* harmony default export */ __webpack_exports__["a"] = ({
    name: "destinationsPage",
    props: ['profile', 'destinations', 'destinationTypes'],
    created() {
        document.title = "TravelEA - Destinations";
    },
    components: {
        SearchDestinations: __WEBPACK_IMPORTED_MODULE_0__searchDestinations_vue__["a" /* default */],
        AddDestinations: __WEBPACK_IMPORTED_MODULE_1__addDestinations_vue__["a" /* default */],
        NavBarMain: __WEBPACK_IMPORTED_MODULE_2__helperComponents_navbarMain_vue__["a" /* default */],
        FooterMain: __WEBPACK_IMPORTED_MODULE_3__helperComponents_footerMain_vue__["a" /* default */],
        UnauthorisedPrompt: __WEBPACK_IMPORTED_MODULE_4__helperComponents_unauthorisedPromptPage__["a" /* default */]
    },
    mounted() {},
    data: function () {
        return {
            searchDestinations: true,
            addDestinations: false

        };
    },
    methods: {
        togglePage: function (viewPage) {
            if (!viewPage) {
                this.searchDestinations = !this.searchDestinations;
                this.addDestinations = !this.addDestinations;
            }
        }
    }
});

/***/ }),

/***/ 148:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["a"] = ({
    name: "searchDestinations",
    props: ['destinationTypes'],
    data() {
        return {
            sortBy: 'name',
            sortDesc: false,
            searchName: "",
            destinations: [],
            searchType: "",
            searchDistrict: "",
            searchLat: "",
            searchLong: "",
            searchCountry: "",
            showError: false,
            optionViews: [{ value: 1, text: "1" }, { value: 5, text: "5" }, { value: 10, text: "10" }, {
                value: 15,
                text: "15"
            }],
            perPage: 10,
            currentPage: 1,
            fields: [{ key: 'name', value: 'name', sortable: true }, {
                key: 'type.destinationType',
                label: 'Type',
                sortable: true
            }, { key: 'district', value: 'district', sortable: true }, 'latitude', 'longitude', {
                key: 'country',
                value: 'country',
                sortable: true
            }],
            searchDestination: "",
            errorMessage: "",
            retrievingDestinations: false
        };
    },
    computed: {
        rows() {
            return this.destinations.length;
        }

    },
    mounted() {
        this.queryDestinations();
    },
    methods: {
        checkLatLong() {
            let ok = true;
            if (isNaN(this.dLatitude)) {
                this.showError = true;
                this.errorMessage = "'" + this.dLatitude + "' is not a number!";
                ok = false;
            } else if (isNaN(this.dLongitude)) {
                this.showError = true;
                this.errorMessage = "'" + this.dLongitude + "' is not a number!";
                ok = false;
            }
            return ok;
        },
        searchDestinations() {
            if (this.checkLatLong) {
                this.searchDestination = {
                    name: this.searchName,
                    type: this.searchType,
                    district: this.searchDistrict,
                    country: this.searchCountry
                };
                this.queryDestinations();
            }
        },
        queryDestinations() {
            this.retrievingDestinations = true;
            let searchQuery = "?name=" + this.searchName + "&type_id=" + this.searchType + "&district=" + this.searchDistrict + "&latitude=" + this.searchLat + "&longitude=" + this.searchLong + "&country=" + this.searchCountry;
            return fetch(`/v1/destinations` + searchQuery, {
                dataType: 'html'
            }).then(this.checkStatus).then(this.parseJSON).then(data => {
                this.destinations = data;
                this.retrievingDestinations = false;
            });
        },
        checkStatus(response) {
            if (response.status >= 200 && response.status < 300) {
                return response;
            }
            const error = new Error(`HTTP Error ${response.statusText}`);
            error.status = response.statusText;
            error.response = response;
            console.log(error);
            throw error;
        },
        parseJSON(response) {
            return response.json();
        }
    }
});

/***/ }),

/***/ 149:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["a"] = ({
    name: "addDestinations",
    props: ['profile', 'destinations', 'destinationTypes'],
    data() {
        return {
            dName: "",
            dType: "",
            dDistrict: "",
            dLatitude: null,
            dLongitude: null,
            dCountry: "",
            showError: false,
            errorMessage: "",
            successTripAddedAlert: false,
            dismissSecs: 3,
            dismissCountDown: 0
        };
    },
    methods: {
        checkLatLong() {
            let ok = true;
            if (isNaN(this.dLatitude)) {
                this.errorMessage = "Latitude: '" + this.dLatitude + "' is not a number!";
                ok = false;
            } else if (isNaN(this.dLongitude)) {
                this.errorMessage = "Longitude: '" + this.dLongitude + "' is not a number!";
                ok = false;
            }
            return ok;
        },
        checkDestinationFields() {
            if (!this.checkLatLong()) {
                this.showError = true;
            } else if (this.dName && this.dDistrict && this.dLatitude && this.dLongitude && this.dCountry) {
                this.showError = false;
                this.addDestination();
            } else {
                this.errorMessage = "Please enter in all fields!";
                this.showError = true;
            }
        },
        resetDestForm() {
            this.dName = "";
            this.dType = "";
            this.dDistrict = "";
            this.dLatitude = "";
            this.dLongitude = "";
            this.dCountry = "";
        },
        addDestination(cb) {
            let self = this;
            let response = fetch(`/v1/destinations`, {
                method: 'POST',
                headers: { 'content-type': 'application/json' },
                body: JSON.stringify({
                    "name": this.dName,
                    "type_id": this.dType,
                    "district": this.dDistrict,
                    "latitude": parseFloat(this.dLatitude),
                    "longitude": parseFloat(this.dLongitude),
                    "country": this.dCountry
                })
            }).then(this.checkStatus).then(this.parseJSON).then(cb).then(function (response) {
                if (response.ok) {
                    self.resetDestForm();
                    self.showAlert();
                    return JSON.parse(JSON.stringify(response));
                } else {
                    throw new Error('Something is wrong!');
                }
            }).catch(() => {
                this.showError = true;
                this.errorMessage = "'" + this.dName + "' already exists as a destination!";
            });
            return response;
        },
        countDownChanged(dismissCountDown) {
            this.dismissCountDown = dismissCountDown;
        },
        showAlert() {
            this.dismissCountDown = this.dismissSecs;
        }
    }
});

/***/ }),

/***/ 150:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__dash_viewProfile_vue__ = __webpack_require__(137);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__dash_dashPage__ = __webpack_require__(135);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__helperComponents_navbarMain_vue__ = __webpack_require__(41);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__helperComponents_footerMain_vue__ = __webpack_require__(42);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__helperComponents_unauthorisedPromptPage__ = __webpack_require__(43);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//







/* harmony default export */ __webpack_exports__["a"] = ({
    name: "profilesPage",
    props: ['profile', 'nationalityOptions', 'travTypeOptions'],
    created() {
        document.title = "TravelEA - Profiles";
    },

    data: function () {
        return {
            sortBy: 'firstName',
            sortDesc: false,
            showError: false,
            searchNationality: "",
            searchGender: "",
            searchMinAge: 0,
            searchMaxAge: 100,
            searchTravType: "",
            optionViews: [{ value: 1, text: "1" }, { value: 5, text: "5" }, { value: 10, text: "10" }, {
                value: 15,
                text: "15"
            }],
            perPage: 5,
            currentPage: 1,
            genderOptions: [{ value: 'male', text: 'Male' }, { value: 'female', text: 'Female' }, { value: 'other', text: 'Other' }],
            fields: [{ key: 'firstName', label: "First Name", sortable: true }, {
                key: 'lastName',
                label: "Last Name",
                sortable: true
            }, { key: 'nationalities[0].nationality', label: "Nationality", sortable: true }, {
                key: 'gender',
                value: 'gender',
                sortable: true
            }, { key: 'age', value: 'age', sortable: true }, {
                key: 'travellerTypes[0].travellerType',
                label: "Traveller Types",
                sortable: true
            }, 'actions'],
            profiles: [],
            retrievingProfiles: false
        };
    },
    mounted() {
        this.queryProfiles();
    },
    methods: {
        parseJSON(response) {
            return response.json();
        },
        searchProfiles() {
            this.searchMinAge = parseInt(this.searchMinAge);
            this.searchMaxAge = parseInt(this.searchMaxAge);
            if (isNaN(this.searchMinAge) || isNaN(this.searchMaxAge)) {
                this.showError = true;
            } else if (this.searchMinAge > this.searchMaxAge) {
                this.showError = true;
            } else {
                if (this.searchTravType === null) {
                    this.searchTravType = "";
                }
                if (this.searchNationality === null) {
                    this.searchNationality = "";
                }
                if (this.searchGender === null) {
                    this.searchGender = "";
                }
                this.showError = false;
                this.queryProfiles();
            }
        },
        queryProfiles() {
            this.retrievingProfiles = true;
            let searchQuery = "?nationality=" + this.searchNationality + "&gender=" + this.searchGender + "&min_age=" + this.searchMinAge + "&max_age=" + this.searchMaxAge + "&traveller_type=" + this.searchTravType;
            return fetch(`/v1/profiles` + searchQuery, {}).then(this.checkStatus).then(this.parseJSON).then(data => {
                this.retrievingProfiles = false;
                this.profiles = data;
            });
        }
    },
    computed: {
        rows() {
            return this.profiles.length;
        }
    },
    components: {
        viewProfile: __WEBPACK_IMPORTED_MODULE_0__dash_viewProfile_vue__["a" /* default */],
        NavBarMain: __WEBPACK_IMPORTED_MODULE_2__helperComponents_navbarMain_vue__["a" /* default */],
        FooterMain: __WEBPACK_IMPORTED_MODULE_3__helperComponents_footerMain_vue__["a" /* default */],
        Dash: __WEBPACK_IMPORTED_MODULE_1__dash_dashPage__["a" /* default */],
        UnauthorisedPrompt: __WEBPACK_IMPORTED_MODULE_4__helperComponents_unauthorisedPromptPage__["a" /* default */]
    }
});

/***/ }),

/***/ 151:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__planATrip_vue__ = __webpack_require__(141);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__yourTrips_vue__ = __webpack_require__(139);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__helperComponents_navbarMain_vue__ = __webpack_require__(41);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__helperComponents_footerMain_vue__ = __webpack_require__(42);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__helperComponents_unauthorisedPromptPage__ = __webpack_require__(43);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//







/* harmony default export */ __webpack_exports__["a"] = ({
    name: "Trips",
    props: ['profile', 'destinations'],
    created() {
        document.title = "TravelEA - Trips";
    },
    data: function () {
        return {
            planATrip: true,
            yourTrips: false
        };
    },
    mounted() {},
    methods: {
        togglePage: function (viewPage) {
            if (!viewPage) {
                this.planATrip = !this.planATrip;
                this.yourTrips = !this.yourTrips;
            }
        }
    },
    components: {
        PlanATrip: __WEBPACK_IMPORTED_MODULE_0__planATrip_vue__["a" /* default */],
        YourTrips: __WEBPACK_IMPORTED_MODULE_1__yourTrips_vue__["a" /* default */],
        NavBarMain: __WEBPACK_IMPORTED_MODULE_2__helperComponents_navbarMain_vue__["a" /* default */],
        FooterMain: __WEBPACK_IMPORTED_MODULE_3__helperComponents_footerMain_vue__["a" /* default */],
        UnauthorisedPrompt: __WEBPACK_IMPORTED_MODULE_4__helperComponents_unauthorisedPromptPage__["a" /* default */]
    }
});

/***/ }),

/***/ 153:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue__ = __webpack_require__(44);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_vue__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__App_vue__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_bootstrap_vue__ = __webpack_require__(168);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_bootstrap_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_bootstrap_vue__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__router__ = __webpack_require__(333);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_bootstrap_dist_css_bootstrap_css__ = __webpack_require__(355);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_bootstrap_dist_css_bootstrap_css___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_bootstrap_dist_css_bootstrap_css__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_bootstrap_vue_dist_bootstrap_vue_css__ = __webpack_require__(356);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_bootstrap_vue_dist_bootstrap_vue_css___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5_bootstrap_vue_dist_bootstrap_vue_css__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6_vue_router__ = __webpack_require__(152);









__WEBPACK_IMPORTED_MODULE_0_vue___default.a.use(__WEBPACK_IMPORTED_MODULE_6_vue_router__["a" /* default */]);

__WEBPACK_IMPORTED_MODULE_0_vue___default.a.config.productionTip = false;
__WEBPACK_IMPORTED_MODULE_0_vue___default.a.use(__WEBPACK_IMPORTED_MODULE_2_bootstrap_vue___default.a);

new __WEBPACK_IMPORTED_MODULE_0_vue___default.a({
    el: '#app',
    router: __WEBPACK_IMPORTED_MODULE_3__router__["a" /* default */],
    template: '<App/>',
    components: { App: __WEBPACK_IMPORTED_MODULE_1__App_vue__["a" /* default */] }
});

/***/ }),

/***/ 157:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_App_vue__ = __webpack_require__(69);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_8f5e20b8_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_App_vue__ = __webpack_require__(167);
function injectStyle (ssrContext) {
  __webpack_require__(158)
}
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = injectStyle
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_App_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_8f5e20b8_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_App_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 158:
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),

/***/ 160:
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),

/***/ 161:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_signup_vue__ = __webpack_require__(72);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_86a173ca_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_signup_vue__ = __webpack_require__(162);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_signup_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_86a173ca_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_signup_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 162:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[(_vm.showFirst)?_c('div',{attrs:{"id":"firstSignup"}},[_c('b-form',[_c('b-form-group',{attrs:{"id":"fname-field","label":"First Name(s):","label-for":"first_name"}},[_c('b-form-input',{attrs:{"id":"first_name","type":"text","state":_vm.fNameValidation,"trim":"","autofocus":"","required":""},model:{value:(_vm.first_name),callback:function ($$v) {_vm.first_name=$$v},expression:"first_name"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.fNameValidation}},[_vm._v(" Invalid! Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.fNameValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"mname-field","label":"Middle Name(s):","label-for":"middle_name"}},[_c('b-form-input',{attrs:{"id":"middle_name","type":"text","state":_vm.mNameValidation,"trim":"","placeholder":"Optional"},model:{value:(_vm.middle_name),callback:function ($$v) {_vm.middle_name=$$v},expression:"middle_name"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.mNameValidation}},[_vm._v(" Invalid! Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.mNameValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"lname-field","label":"Last Name(s):","label-for":"last_name"}},[_c('b-form-input',{attrs:{"id":"last_name","type":"text","state":_vm.lNameValidation,"trim":"","required":""},model:{value:(_vm.last_name),callback:function ($$v) {_vm.last_name=$$v},expression:"last_name"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.lNameValidation}},[_vm._v(" Invalid! Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.lNameValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"email-field","description":"Note: this will be your username","label":"Email:","label-for":"email"}},[_c('b-form-input',{attrs:{"id":"email","type":"text","state":_vm.emailValidation,"trim":"","required":""},model:{value:(_vm.email),callback:function ($$v) {_vm.email=$$v},expression:"email"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.emailValidation}},[_vm._v(" Invalid! Please enter a valid email!\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.emailValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"password-field","label":"Password:","label-for":"newPassword"}},[_c('b-form-input',{attrs:{"id":"newPassword","type":"password","state":_vm.passwordValidation,"trim":"","required":""},model:{value:(_vm.password),callback:function ($$v) {_vm.password=$$v},expression:"password"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.passwordValidation}},[_vm._v(" Your password is weak You must have at least 2 of the following: lowercase letters, uppercase letters, numbers. Password must also be between 5 and 15 characters long.\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.passwordValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"rePassword-field","description":"Please re-enter your password","label":"Retype Password:","label-for":"rePassword"}},[_c('b-form-input',{attrs:{"id":"rePassword","type":"password","state":_vm.rePasswordValidation,"trim":"","required":""},model:{value:(_vm.rePassword),callback:function ($$v) {_vm.rePassword=$$v},expression:"rePassword"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.rePasswordValidation}},[_vm._v(" Passwords do not match! Please ensure this matches your password!\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.rePasswordValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"dateOfBirth-field","label":"Date of Birth:","label-for":"dateOfBirth"}},[_c('b-form-input',{attrs:{"id":"dateOfBirth","type":"date","state":_vm.dateOfBirthValidation,"trim":"","required":""},model:{value:(_vm.dateOfBirth),callback:function ($$v) {_vm.dateOfBirth=$$v},expression:"dateOfBirth"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.dateOfBirthValidation}},[_vm._v(" You need a date of birth!\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.dateOfBirthValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"gender-field","label":"Gender:","label-for":"gender"}},[_c('b-form-select',{attrs:{"id":"gender","options":_vm.genderOptions,"state":_vm.genderValidation,"trim":"","required":""},model:{value:(_vm.gender),callback:function ($$v) {_vm.gender=$$v},expression:"gender"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.genderValidation}},[_vm._v(" Please select a gender!\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.genderValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-alert',{attrs:{"variant":"danger","dismissible":""},model:{value:(_vm.showError),callback:function ($$v) {_vm.showError=$$v},expression:"showError"}},[_vm._v("The form contains errors!")]),_vm._v(" "),_c('b-button',{attrs:{"variant":"primary","block":""},on:{"click":_vm.checkPersonalForm}},[_vm._v("Next")])],1)],1):_vm._e(),_vm._v(" "),(_vm.showSecond)?_c('div',{attrs:{"id":"secondSignup"}},[_c('b-form',[_c('b-row',[_c('b-col',[_c('b-form-group',{attrs:{"id":"nationalities-field","label":"Nationality:","label-for":"nationality"}},[_c('b-form-select',{attrs:{"id":"nationality","state":_vm.nationalityValidation,"multiple":"","trim":""},model:{value:(_vm.nationalities),callback:function ($$v) {_vm.nationalities=$$v},expression:"nationalities"}},_vm._l((_vm.nationalityOptions),function(nationality){return _c('option',{domProps:{"value":nationality.id}},[_vm._v("\n                                "+_vm._s(nationality.nationality)+"\n                            ")])}),0),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.nationalityValidation}},[_vm._v(" Please select at least one\n                            nationality.\n                        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.nationalityValidation}},[_vm._v(" Looks Good")])],1)],1),_vm._v(" "),_c('b-col',[_c('b-form-group',{attrs:{"id":"passports-field","label":"Passport Country:","label-for":"passports"}},[_c('b-form-select',{attrs:{"id":"passports","state":_vm.passportValidation,"trim":"","multiple":""},model:{value:(_vm.passports),callback:function ($$v) {_vm.passports=$$v},expression:"passports"}},_vm._l((_vm.nationalityOptions),function(nationality){return _c('option',{domProps:{"value":nationality.id}},[_vm._v("\n                                "+_vm._s(nationality.country)+"\n                            ")])}),0),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.passportValidation}},[_vm._v(" Please select at least one passport\n                            country.\n                        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.passportValidation}},[_vm._v(" Looks Good")])],1)],1)],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"travType-field","label":"Traveller Type:","label-for":"travellerTypeCarousel"}},[_c('b-carousel',{staticStyle:{"text-shadow":"1px 1px 2px #333"},attrs:{"id":"travellerTypeCarousel","interval":10000,"controls":"","indicators":"","background":"#ababab","img-width":"1920","img-height":"1080"}},_vm._l((_vm.travTypeOptions),function(travType){return _c('b-carousel-slide',{key:travType.id,attrs:{"caption":travType.travellerType,"text":travType.description,"img-src":travType.imgUrl,"state":_vm.travTypeValidation}},[_c('b-form-checkbox',{attrs:{"value":travType.id},model:{value:(_vm.travTypes),callback:function ($$v) {_vm.travTypes=$$v},expression:"travTypes"}})],1)}),1),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.travTypeValidation,"align":"center"}},[_vm._v(" Please select at least one\n                    traveller type.\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.travTypeValidation,"align":"center"}},[_vm._v(" Looks Good\n                ")])],1),_vm._v(" "),_c('b-button',{on:{"click":_vm.previousPage}},[_vm._v("Back")]),_vm._v(" "),_c('b-button',{staticClass:"float-right",attrs:{"variant":"primary"},on:{"click":_vm.checkAssociateForm}},[_vm._v("Sign Up")])],1)],1):_vm._e()])}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 163:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_login_vue__ = __webpack_require__(73);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_3049c274_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_login_vue__ = __webpack_require__(164);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_login_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_3049c274_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_login_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 164:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[_c('b-form',[_c('b-alert',{attrs:{"variant":"danger","dismissible":""},model:{value:(_vm.showError),callback:function ($$v) {_vm.showError=$$v},expression:"showError"}},[_vm._v("Invalid Username or Password")]),_vm._v(" "),_c('b-form-group',{attrs:{"id":"username-field","description":"Please enter your username (email)","label":"Username","label-for":"username"}},[_c('b-form-input',{attrs:{"id":"username","autofocus":"","trim":""},model:{value:(_vm.username),callback:function ($$v) {_vm.username=$$v},expression:"username"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"password-field","description":"Please enter your password","label":"Password","label-for":"password"}},[_c('b-form-input',{attrs:{"id":"password","type":'password',"trim":""},model:{value:(_vm.password),callback:function ($$v) {_vm.password=$$v},expression:"password"}})],1),_vm._v(" "),_c('b-button',{attrs:{"id":"sign-in","variant":"primary","block":""},on:{"click":_vm.login}},[_vm._v("Sign In")])],1)],1)}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 165:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[_c('div',{staticClass:"bg"},[_c('div',{staticStyle:{"padding-top":"10%"}},[_c('div',[_vm._m(0),_vm._v(" "),_c('b-row',[_c('b-col',[_c('b-button',{directives:[{name:"b-modal",rawName:"v-b-modal.modalSignup",modifiers:{"modalSignup":true}}],staticClass:"btn btn-info btn-lg float-right"},[_vm._v("Signup")]),_vm._v(" "),_c('b-modal',{attrs:{"id":"modalSignup","centered":"","hide-footer":""}},[_c('template',{slot:"modal-title"},[_c('h2',[_vm._v("Sign Up")])]),_vm._v(" "),_c('signup',{attrs:{"nationalityOptions":_vm.nationalityOptions,"travTypeOptions":_vm.travTypeOptions}})],2)],1),_vm._v(" "),_c('b-col',[_c('b-button',{directives:[{name:"b-modal",rawName:"v-b-modal.modalLogin",modifiers:{"modalLogin":true}}],staticClass:"btn btn-info btn-lg float-left"},[_vm._v("Login")]),_vm._v(" "),_c('b-modal',{attrs:{"id":"modalLogin","hide-footer":"","centered":"","title":"Login"}},[_c('template',{slot:"modal-title"},[_c('h2',[_vm._v("Login")])]),_vm._v(" "),_c('login')],2)],1)],1)],1)])])])}
var staticRenderFns = [function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{attrs:{"align":"center"}},[_c('img',{attrs:{"src":__webpack_require__(166),"width":"50%","alt":"TravelEA Logo"}}),_vm._v(" "),_c('h3',{attrs:{"id":"subtitle","align":"center"}},[_vm._v("Your personal Travel Executive Assistant!")])])}]
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 166:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/full_logo_lg.8ec28bc.png";

/***/ }),

/***/ 167:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"App"},[_c('div',[_c('router-view',{attrs:{"profile":_vm.profile,"destinations":_vm.destinations,"destinationTypes":_vm.destinationTypes,"nationalityOptions":_vm.nationalityOptions,"travTypeOptions":_vm.travTypeOptions}})],1)])}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 333:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__components_index_indexPage__ = __webpack_require__(70);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__components_dash_dashPage__ = __webpack_require__(135);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__components_destinations_destinationsPage__ = __webpack_require__(345);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_profiles_profilesPage__ = __webpack_require__(351);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__components_trips_tripsPage__ = __webpack_require__(353);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_vue_router__ = __webpack_require__(152);








const routes = [{
    path: "/",
    name: "index",
    component: __WEBPACK_IMPORTED_MODULE_0__components_index_indexPage__["a" /* default */]
}, {
    path: "/dash",
    name: "dash",
    component: __WEBPACK_IMPORTED_MODULE_1__components_dash_dashPage__["a" /* default */]
}, {
    path: "/destinations",
    name: "destinations",
    component: __WEBPACK_IMPORTED_MODULE_2__components_destinations_destinationsPage__["a" /* default */]
}, {
    path: "/profiles",
    name: "profiles",
    component: __WEBPACK_IMPORTED_MODULE_3__components_profiles_profilesPage__["a" /* default */]
}, {
    path: "/trips",
    name: "trips",
    component: __WEBPACK_IMPORTED_MODULE_4__components_trips_tripsPage__["a" /* default */]
}];

const router = new __WEBPACK_IMPORTED_MODULE_5_vue_router__["a" /* default */]({
    routes: routes,
    mode: 'history'
});

/* harmony default export */ __webpack_exports__["a"] = (router);

/***/ }),

/***/ 334:
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),

/***/ 335:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"container"},[_c('h1',{staticClass:"page_title"},[_vm._v(_vm._s(_vm.heading))]),_vm._v(" "),_vm._m(0),_vm._v(" "),_c('b-alert',{attrs:{"variant":"danger","dismissible":""},model:{value:(_vm.showError),callback:function ($$v) {_vm.showError=$$v},expression:"showError"}},[_vm._v(_vm._s(_vm.errorMessage))]),_vm._v(" "),_c('b-alert',{attrs:{"show":_vm.dismissCountDown,"dismissible":"","variant":"success"},on:{"dismissed":function($event){_vm.dismissCountDown=0},"dismiss-count-down":_vm.countDownChanged}},[_c('p',[_vm._v("Trip Successfully Added")]),_vm._v(" "),_c('b-progress',{attrs:{"variant":"success","max":_vm.dismissSecs,"value":_vm.dismissCountDown,"height":"4px"}})],1),_vm._v(" "),_c('b-modal',{ref:"editModal",attrs:{"id":"editModal","hide-footer":"","title":"Edit Destination"}},[_c('div',{staticClass:"d-block"},[_c('b-form-group',{attrs:{"id":"editInDate-field","label":"In Date:","label-for":"editInDate"}},[_c('b-input',{attrs:{"id":"editInDate","type":'date',"max":"9999-12-31"},model:{value:(_vm.editInDate),callback:function ($$v) {_vm.editInDate=$$v},expression:"editInDate"}},[_vm._v(_vm._s(_vm.editInDate)+" trim\n                ")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"editOutDate-field","label":"Out Date:","label-for":"editOutDate"}},[_c('b-input',{attrs:{"id":"editOutDate","type":'date',"max":"9999-12-31"},model:{value:(_vm.editOutDate),callback:function ($$v) {_vm.editOutDate=$$v},expression:"editOutDate"}},[_vm._v(_vm._s(_vm.editOutDate)+"\n                    trim\n                ")])],1)],1),_vm._v(" "),_c('b-button',{staticClass:"mr-2 float-right",attrs:{"variant":"success"},on:{"click":function($event){_vm.saveDestination(_vm.rowEdit, _vm.editInDate, _vm.editOutDate); _vm.dismissModal; _vm.dismissCountDown}}},[_vm._v("Save\n        ")]),_vm._v(" "),_c('b-button',{staticClass:"mr-2 float-right",attrs:{"variant":"danger"},on:{"click":_vm.dismissModal}},[_vm._v("Cancel")])],1),_vm._v(" "),_c('b-form',[_c('b-container',{attrs:{"fluid":""}},[_c('b-form-group',{attrs:{"id":"trip_name-field","label":"Trip Name:","label-for":"trip_name"}},[_c('b-form-input',{attrs:{"id":"trip_name","type":'text',"trim":""},model:{value:(_vm.tripName),callback:function ($$v) {_vm.tripName=$$v},expression:"tripName"}})],1)],1),_vm._v(" "),_c('b-form',{on:{"reset":_vm.resetDestForm}},[_c('b-container',[_c('b-row',[_c('b-col',[_c('b-form-group',{attrs:{"id":"destination-field","label":"Add a Destination:","label-for":"destination"}},[_c('b-form-select',{attrs:{"id":"destination","type":'text',"trim":""},model:{value:(_vm.tripDestination),callback:function ($$v) {_vm.tripDestination=$$v},expression:"tripDestination"}},_vm._l((_vm.destinations),function(destination){return _c('option',{domProps:{"value":destination}},[_vm._v("\n                                    "+_vm._s(destination.name)+"\n                                ")])}),0)],1)],1),_vm._v(" "),_c('b-col',[_c('b-form-group',{attrs:{"id":"inDate-field","label":"In Date (optional):","label-for":"inDate"}},[_c('b-form-input',{attrs:{"id":"inDate","type":'date',"max":"9999-12-31","trim":""},model:{value:(_vm.inDate),callback:function ($$v) {_vm.inDate=$$v},expression:"inDate"}})],1)],1),_vm._v(" "),_c('b-col',[_c('b-form-group',{attrs:{"id":"outDate-field","label":"Out Date (optional):","label-for":"outDate"}},[_c('b-form-input',{attrs:{"id":"outDate","type":'date',"max":"9999-12-31","trim":""},model:{value:(_vm.outDate),callback:function ($$v) {_vm.outDate=$$v},expression:"outDate"}})],1)],1)],1),_vm._v(" "),_c('b-button',{staticClass:"mr-2 float-right",attrs:{"variant":"primary"},on:{"click":_vm.checkDestination}},[_vm._v("Add Destination\n                ")])],1)],1)],1),_vm._v(" "),_c('b-table',{ref:"tripDestTable",attrs:{"hover":"","striped":"","outlined":"","id":"myTrips","fields":_vm.fields,"items":_vm.tripDestinations,"per-page":_vm.perPage,"current-page":_vm.currentPage},scopedSlots:_vm._u([{key:"actions",fn:function(row){return [_c('b-button',{directives:[{name:"b-modal",rawName:"v-b-modal.editModal",modifiers:{"editModal":true}}],staticClass:"mr-2",attrs:{"size":"sm"},on:{"click":function($event){return _vm.populateModal(row.item)}}},[_vm._v("Edit")]),_vm._v(" "),_c('b-button',{staticClass:"mr-2",attrs:{"size":"sm"},on:{"click":row.toggleDetails}},[_vm._v("\n                "+_vm._s(row.detailsShowing ? 'Hide' : 'Show')+" Details\n            ")]),_vm._v(" "),_c('b-button',{staticClass:"mr-2",attrs:{"size":"sm","variant":"danger"},on:{"click":function($event){return _vm.deleteDestination(row.item, row.index)}}},[_vm._v("Delete\n            ")])]}},{key:"order",fn:function(row){return [_c('b-button',{staticClass:"mr-2",attrs:{"size":"sm","disabled":_vm.tripDestinations.length === 1 || row.index === 0,"variant":"success"},on:{"click":function($event){return _vm.moveUpCheck(row.index)}}},[_vm._v("↑\n            ")]),_vm._v(" "),_c('b-button',{staticClass:"mr-2",attrs:{"size":"sm","disabled":_vm.tripDestinations.length === 1 || row.index === _vm.tripDestinations.length-1,"variant":"success"},on:{"click":function($event){return _vm.moveDownCheck(row.index)}}},[_vm._v("↓\n            ")])]}},{key:"row-details",fn:function(row){return [_c('b-card',[_c('b-row',{staticClass:"mb-2"},[_c('b-col',{staticClass:"text-sm-right",attrs:{"sm":"3"}},[_c('b',[_vm._v("Type:")])]),_vm._v(" "),_c('b-col',[_vm._v(_vm._s(row.item.type))])],1),_vm._v(" "),_c('b-row',{staticClass:"mb-2"},[_c('b-col',{staticClass:"text-sm-right",attrs:{"sm":"3"}},[_c('b',[_vm._v("District:")])]),_vm._v(" "),_c('b-col',[_vm._v(_vm._s(row.item.district))])],1),_vm._v(" "),_c('b-row',{staticClass:"mb-2"},[_c('b-col',{staticClass:"text-sm-right",attrs:{"sm":"3"}},[_c('b',[_vm._v("Latitude:")])]),_vm._v(" "),_c('b-col',[_vm._v(_vm._s(row.item.latitude))])],1),_vm._v(" "),_c('b-row',{staticClass:"mb-2"},[_c('b-col',{staticClass:"text-sm-right",attrs:{"sm":"3"}},[_c('b',[_vm._v("Longitude:")])]),_vm._v(" "),_c('b-col',[_vm._v(_vm._s(row.item.longitude))])],1),_vm._v(" "),_c('b-row',{staticClass:"mb-2"},[_c('b-col',{staticClass:"text-sm-right",attrs:{"sm":"3"}},[_c('b',[_vm._v("Country:")])]),_vm._v(" "),_c('b-col',[_vm._v(_vm._s(row.item.country))])],1)],1)]}}])}),_vm._v(" "),_c('b-row',[_c('b-col',{attrs:{"cols":"1"}},[_c('b-form-group',{attrs:{"id":"numItems-field","label-for":"perPage"}},[_c('b-form-select',{attrs:{"id":"perPage","options":_vm.optionViews,"size":"sm","trim":""},model:{value:(_vm.perPage),callback:function ($$v) {_vm.perPage=$$v},expression:"perPage"}})],1)],1),_vm._v(" "),_c('b-col',{attrs:{"cols":"8"}},[_c('b-pagination',{attrs:{"total-rows":_vm.rows,"per-page":_vm.perPage,"aria-controls":"my-table","first-text":"First","last-text":"Last","align":"center","size":"sm"},model:{value:(_vm.currentPage),callback:function ($$v) {_vm.currentPage=$$v},expression:"currentPage"}})],1)],1),_vm._v(" "),_c('b-button',{staticClass:"mr-2 float-right",attrs:{"variant":"primary","block":""},on:{"click":_vm.submitTrip}},[(_vm.savingTrip)?_c('b-spinner',{attrs:{"small":"","variant":"dark","label":"Spinning"}},[_vm._v("Saving...")]):_vm._e(),_vm._v("\n        Save Trip\n    ")],1)],1)}
var staticRenderFns = [function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('p',{staticClass:"page_title"},[_c('i',[_vm._v("Book your next trip!")])])}]
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 336:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"container"},[_c('div',{attrs:{"id":"upcomingTrips"}},[_c('h1',{staticClass:"page_title"},[_vm._v("Upcoming Trips")]),_vm._v(" "),_vm._m(0),_vm._v(" "),_c('b-table',{ref:"myFutureTrips",attrs:{"hover":"","striped":"","outlined":"","id":"myFutureTrips","items":_vm.trips,"fields":_vm.fields,"per-page":_vm.perPageUpcoming,"current-page":_vm.currentPageUpcoming,"busy":_vm.trips.length === 0},scopedSlots:_vm._u([{key:"more_details",fn:function(row){return [_c('b-button',{staticClass:"mr-2",attrs:{"size":"sm"},on:{"click":row.toggleDetails}},[_vm._v("\n                    "+_vm._s(row.detailsShowing ? 'Hide' : 'Show')+" Details\n                ")])]}},{key:"row-details",fn:function(row){return [_c('b-card',[_c('b-table',{attrs:{"id":"futureTripsDestinations","items":row.item.destinations,"fields":_vm.subFields}})],1)]}}])},[_c('div',{staticClass:"text-center my-2",attrs:{"slot":"table-busy"},slot:"table-busy"},[(_vm.retrievingTrips)?_c('b-spinner',{staticClass:"align-middle"}):_vm._e(),_vm._v(" "),_c('strong',[_vm._v("Can't find any trips!")])],1)]),_vm._v(" "),_c('b-row',[_c('b-col',{attrs:{"cols":"1"}},[_c('b-form-group',{attrs:{"id":"numUpcomingtems-field","label-for":"perPageUpcoming"}},[_c('b-form-select',{attrs:{"id":"perPageUpcoming","options":_vm.optionViews,"size":"sm","trim":""},model:{value:(_vm.perPageUpcoming),callback:function ($$v) {_vm.perPageUpcoming=$$v},expression:"perPageUpcoming"}})],1)],1),_vm._v(" "),_c('b-col',{attrs:{"cols":"8"}},[_c('b-pagination',{attrs:{"total-rows":_vm.rowsUpcoming,"per-page":_vm.perPageUpcoming,"aria-controls":"my-table","first-text":"First","last-text":"Last","align":"center","size":"sm"},model:{value:(_vm.currentPageUpcoming),callback:function ($$v) {_vm.currentPageUpcoming=$$v},expression:"currentPageUpcoming"}})],1)],1)],1),_vm._v(" "),_c('div',{attrs:{"id":"pastTrips"}},[_c('h1',{staticClass:"page_title"},[_vm._v("Past Trips")]),_vm._v(" "),_vm._m(1),_vm._v(" "),_c('b-table',{ref:"myPastTrips",attrs:{"hover":"","striped":"","outlined":"","id":"myPastTrips","items":_vm.pastTrips,"fields":_vm.fields,"per-page":_vm.perPagePast,"current-page":_vm.currentPagePast,"sort-by":_vm.sortBy,"sort-desc":false}}),_vm._v(" "),_c('b-row',[_c('b-col',{attrs:{"cols":"1"}},[_c('b-form-group',{attrs:{"id":"numItemsPast-field","label-for":"perPagePast"}},[_c('b-form-select',{attrs:{"id":"perPage","options":_vm.optionViews,"size":"sm","trim":""},model:{value:(_vm.perPagePast),callback:function ($$v) {_vm.perPagePast=$$v},expression:"perPagePast"}})],1)],1),_vm._v(" "),_c('b-col',{attrs:{"cols":"8"}},[_c('b-pagination',{attrs:{"total-rows":_vm.rowsPast,"per-page":_vm.perPagePast,"aria-controls":"my-table","first-text":"First","last-text":"Last","align":"center","size":"sm"},model:{value:(_vm.currentPagePast),callback:function ($$v) {_vm.currentPagePast=$$v},expression:"currentPagePast"}})],1)],1)],1)])}
var staticRenderFns = [function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('p',{staticClass:"page_title"},[_c('i',[_vm._v("Here are your upcoming trips!")])])},function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('p',{staticClass:"page_title"},[_c('i',[_vm._v("Here are your past trips!")])])}]
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 337:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"container"},[_c('h1',[_vm._v(_vm._s(_vm.profile.firstName)+" "+_vm._s(_vm.profile.middleName)+" "+_vm._s(_vm.profile.lastName))]),_vm._v(" "),_c('h2',[_vm._v("Personal Details")]),_vm._v(" "),_c('p',[_vm._v(" Username: "+_vm._s(_vm.profile.username))]),_vm._v(" "),_c('p',[_vm._v(" Date of Creation: "+_vm._s(new Date(_vm.profile.dateOfCreation).toUTCString()))]),_vm._v(" "),_c('p',[_vm._v(" Date of Birth: "+_vm._s(new Date(_vm.profile.dateOfBirth).toLocaleDateString()))]),_vm._v(" "),_c('p',[_vm._v(" Gender: "+_vm._s(_vm.profile.gender))]),_vm._v(" "),_c('h2',[_vm._v(" Nationalities ")]),_vm._v(" "),_c('ul',_vm._l((_vm.profile.nationalities),function(nationality){return _c('li',[_vm._v(_vm._s(nationality.nationality))])}),0),_vm._v(" "),_c('h2',[_vm._v(" Passports ")]),_vm._v(" "),_c('ul',_vm._l((_vm.profile.passports),function(passport){return _c('li',[_vm._v(_vm._s(passport.country))])}),0),_vm._v(" "),_c('h2',[_vm._v(" Traveller Types ")]),_vm._v(" "),_c('ul',_vm._l((_vm.profile.travellerTypes),function(travType){return _c('li',[_vm._v(_vm._s(travType.travellerType))])}),0),_vm._v(" "),_c('your-trips',{attrs:{"profile":_vm.profile}})],1)}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 338:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_editProfile_vue__ = __webpack_require__(143);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_54fa345b_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_editProfile_vue__ = __webpack_require__(339);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_editProfile_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_54fa345b_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_editProfile_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 339:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"container"},[_c('b-form-group',{attrs:{"id":"first_name-field","label":"First Name(s):","label-for":"first_name"}},[_c('b-form-input',{attrs:{"id":"first_name","state":_vm.fNameValidation,"type":"text","trim":""},model:{value:(_vm.saveProfile.first_name),callback:function ($$v) {_vm.$set(_vm.saveProfile, "first_name", $$v)},expression:"saveProfile.first_name"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.fNameValidation}},[_vm._v(" A banner saying `Invalid! Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.fNameValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"middle_name-field","label":"Middle Name(s):","label-for":"middle_name"}},[_c('b-form-input',{attrs:{"id":"middle_name","state":_vm.mNameValidation,"type":"text","trim":""},model:{value:(_vm.saveProfile.middle_mame),callback:function ($$v) {_vm.$set(_vm.saveProfile, "middle_mame", $$v)},expression:"saveProfile.middle_mame"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.mNameValidation}},[_vm._v(" A banner saying `Invalid! Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.mNameValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"last_name-field","label":"Last Name(s):","label-for":"last_name"}},[_c('b-form-input',{attrs:{"id":"last_name","state":_vm.lNameValidation,"type":"text","trim":""},model:{value:(_vm.saveProfile.last_name),callback:function ($$v) {_vm.$set(_vm.saveProfile, "last_name", $$v)},expression:"saveProfile.last_name"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.lNameValidation}},[_vm._v(" A banner saying `Invalid! Must be 1-100 characters long. Hyphens and apostrophes must be used correctly!\n        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.lNameValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"email-field","label":"Email:","label-for":"email"}},[_c('b-form-input',{attrs:{"id":"email","state":_vm.emailValidation,"type":"email","trim":""},model:{value:(_vm.saveProfile.username),callback:function ($$v) {_vm.$set(_vm.saveProfile, "username", $$v)},expression:"saveProfile.username"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.emailValidation}},[_vm._v(" Invalid! Please enter a valid email!\n        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.emailValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"password-field","label":"Password:","label-for":"password"}},[_c('b-form-input',{attrs:{"id":"password","state":_vm.passwordValidation,"type":"password","placeholder":"Unchanged","trim":""},model:{value:(_vm.saveProfile.password),callback:function ($$v) {_vm.$set(_vm.saveProfile, "password", $$v)},expression:"saveProfile.password"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.passwordValidation}},[_vm._v(" Your password is weak You must have at least 2 of the following: lowercase letters, uppercase letters, numbers. Password must also be between 5 and 15 characters long.\n        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.passwordValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"passwordRE-field","label":"Retype Password:","label-for":"passwordre"}},[_c('b-form-input',{attrs:{"id":"passwordre","state":_vm.rePasswordValidation,"type":"password","placeholder":"Unchanged","trim":""},model:{value:(_vm.rePassword),callback:function ($$v) {_vm.rePassword=$$v},expression:"rePassword"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.rePasswordValidation}},[_vm._v(" Passwords do not match! Please ensure this matches your password!\n        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.rePasswordValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"DOB-field","label":"Date of Birth:","label-for":"dateOfBirth"}},[_c('b-form-input',{attrs:{"id":"dateOfBirth","state":_vm.dateOfBirthValidation,"type":'date',"trim":""},model:{value:(_vm.saveProfile.date_of_birth),callback:function ($$v) {_vm.$set(_vm.saveProfile, "date_of_birth", $$v)},expression:"saveProfile.date_of_birth"}}),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.dateOfBirthValidation}},[_vm._v(" You need a date of birth!")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.dateOfBirthValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"gender-field","label":"Gender:","label-for":"gender"}},[_c('b-form-select',{attrs:{"id":"gender","state":_vm.genderValidation,"trim":"","required":""},model:{value:(_vm.saveProfile.gender),callback:function ($$v) {_vm.$set(_vm.saveProfile, "gender", $$v)},expression:"saveProfile.gender"}},_vm._l((_vm.genderOptions),function(gender){return _c('option',[_vm._v(_vm._s(gender.value))])}),0),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.genderValidation}},[_vm._v(" Please select a gender!")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.genderValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-row',[_c('b-col',[_c('b-form-group',{attrs:{"id":"nationalities-field","label":"Nationality:","label-for":"nationality"}},[_c('b-form-select',{attrs:{"id":"nationality","state":_vm.nationalityValidation,"required":true,"multiple":"","trim":""},model:{value:(_vm.saveProfile.nationality),callback:function ($$v) {_vm.$set(_vm.saveProfile, "nationality", $$v)},expression:"saveProfile.nationality"}},[_c('optgroup',{attrs:{"label":"Current Nationalities: (Please select these if you want to use them!)"}},_vm._l((_vm.profile.nationalities),function(nationality){return _c('option',{domProps:{"value":nationality.id}},[_vm._v(_vm._s(nationality.nationality)+"\n                        ")])}),0),_vm._v(" "),_c('optgroup',{attrs:{"label":"Other Nationalities:"}},_vm._l((_vm.nationalityOptions),function(nationality){return (!_vm.duplicateNationality(nationality.id))?_c('option',{domProps:{"value":nationality.id}},[_vm._v("\n                            "+_vm._s(nationality.nationality)+"\n                        ")]):_vm._e()}),0)]),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.nationalityValidation}},[_vm._v(" Please select at least one nationality.\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.nationalityValidation}},[_vm._v(" Looks Good")])],1)],1),_vm._v(" "),_c('b-col',[_c('b-form-group',{attrs:{"id":"passports-field","label":"Passport:","label-for":"passports"}},[_c('b-form-select',{attrs:{"id":"passports","state":_vm.passportValidation,"required":true,"trim":"","multiple":""},model:{value:(_vm.saveProfile.passport_country),callback:function ($$v) {_vm.$set(_vm.saveProfile, "passport_country", $$v)},expression:"saveProfile.passport_country"}},[_c('optgroup',{attrs:{"label":"Current Passports: (Please select these if you want to use them!)"}},_vm._l((_vm.profile.passports),function(passport){return _c('option',{domProps:{"selected":true,"value":passport.id}},[_vm._v("\n                            "+_vm._s(passport.country)+"\n                        ")])}),0),_vm._v(" "),_c('optgroup',{attrs:{"label":"Other Passports:"}},_vm._l((_vm.nationalityOptions),function(nationality){return (!_vm.duplicatePassport(nationality.id))?_c('option',{domProps:{"value":nationality.id}},[_vm._v(_vm._s(nationality.country)+"\n                        ")]):_vm._e()}),0)]),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.passportValidation}},[_vm._v(" Please select at least one passport country.\n                ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.passportValidation}},[_vm._v(" Looks Good")])],1)],1)],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"travType-field","label":"Traveller Type(s):","label-for":"travType"}},[_c('b-form-select',{attrs:{"lg":"","id":"travType","state":_vm.travTypeValidation,"multiple":"","trim":""},model:{value:(_vm.saveProfile.traveller_type),callback:function ($$v) {_vm.$set(_vm.saveProfile, "traveller_type", $$v)},expression:"saveProfile.traveller_type"}},[_c('optgroup',{attrs:{"label":"Current Traveller Types: (Please select these if you want to use them!)"}},_vm._l((_vm.profile.travellerTypes),function(travType){return _c('option',{domProps:{"value":travType.id}},[_vm._v(_vm._s(travType.travellerType)+"\n                ")])}),0),_vm._v(" "),_c('optgroup',{attrs:{"label":"Other Traveller Types"}},_vm._l((_vm.travTypeOptions),function(travType){return (!_vm.duplicateTravType(travType.id))?_c('option',{domProps:{"value":travType.id}},[_vm._v(_vm._s(travType.travellerType)+"\n                ")]):_vm._e()}),0)]),_vm._v(" "),_c('b-form-invalid-feedback',{attrs:{"state":_vm.travTypeValidation}},[_vm._v(" Please select at least one traveller type.\n        ")]),_vm._v(" "),_c('b-form-valid-feedback',{attrs:{"state":_vm.travTypeValidation}},[_vm._v(" Looks Good")])],1),_vm._v(" "),_c('b-alert',{attrs:{"variant":"danger","dismissible":""},model:{value:(_vm.showError),callback:function ($$v) {_vm.showError=$$v},expression:"showError"}},[_vm._v("The form contains errors!")]),_vm._v(" "),_c('b-button',{attrs:{"variant":"success","size":"lg","block":""},on:{"click":_vm.checkSaveProfile}},[_vm._v("Save Profile")])],1)}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 340:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[_c('b-navbar',{attrs:{"variant":"light","toggleable":"lg"}},[_c('b-navbar-brand',{on:{"click":function($event){return _vm.goToProfile()}}},[_c('img',{attrs:{"src":_vm.assets.appLogo}})]),_vm._v(" "),_c('b-navbar-toggle',{attrs:{"target":"nav-collapse"}}),_vm._v(" "),_c('b-collapse',{attrs:{"id":"nav-collapse","is-nav":""}},[_c('b-navbar-nav',[_c('b-nav-item',{class:{active: _vm.currentPage==='/profiles'},on:{"click":function($event){return _vm.goToPeople()}}},[_vm._v("People\n                ")]),_vm._v(" "),_c('b-nav-item',{class:{active: _vm.currentPage==='/trips'},on:{"click":function($event){return _vm.goToTrips()}}},[_vm._v("Trips")]),_vm._v(" "),_c('b-nav-item',{class:{active: _vm.currentPage==='/destinations'},on:{"click":function($event){return _vm.goToDestinations()}}},[_vm._v("\n                    Destinations\n                ")])],1),_vm._v(" "),_c('b-navbar-nav',{staticClass:"ml-auto"},[_c('b-nav-item-dropdown',{attrs:{"right":""}},[_c('template',{slot:"button-content"},[_c('em',[_vm._v(_vm._s(_vm.profile.firstName))])]),_vm._v(" "),_c('b-dropdown-item',{class:{active: _vm.currentPage==='/dash'},on:{"click":function($event){return _vm.goToProfile()}}},[_vm._v("Profile\n                    ")]),_vm._v(" "),_c('b-dropdown-item',{on:{"click":_vm.logout}},[_vm._v("Logout")])],2)],1)],1)],1)],1)}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 341:
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),

/***/ 342:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _vm._m(0)}
var staticRenderFns = [function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('footer',{attrs:{"id":"footer"}},[_c('p',[_vm._v("This is Team 100's TravelEA Website!"),_c('br'),_vm._v("Everyware™")])])}]
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 343:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{attrs:{"align":"center"}},[_c('br'),_vm._v(" "),_c('br'),_vm._v(" "),_c('br'),_vm._v(" "),_c('br'),_vm._v("\n    Oops! You are not logged in!\n    "),_c('br'),_vm._v(" "),_c('b-button',{attrs:{"href":"/"}},[_vm._v("Take me to log in")])],1)}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 344:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return (_vm.profile.length !== 0)?_c('div',[_c('nav-bar-main',{attrs:{"profile":_vm.profile}}),_vm._v(" "),_c('b-navbar',{attrs:{"variant":"light"}},[_c('b-navbar-nav',[_c('b-nav-item',{on:{"click":function($event){return _vm.togglePage(_vm.viewProfile)}}},[_vm._v("Profile")]),_vm._v(" "),_c('b-nav-item',{on:{"click":function($event){return _vm.togglePage(_vm.editProfile)}}},[_vm._v("Edit Profile")])],1)],1),_vm._v(" "),(_vm.viewProfile)?_c('view-profile',{attrs:{"profile":_vm.profile,"nationalityOptions":_vm.nationalityOptions,"travTypeOptions":_vm.travTypeOptions}}):_vm._e(),_vm._v(" "),(_vm.editProfile)?_c('edit-profile',{attrs:{"profile":_vm.profile,"nationalityOptions":_vm.nationalityOptions,"travTypeOptions":_vm.travTypeOptions}}):_vm._e(),_vm._v(" "),_c('footer-main')],1):_c('div',[_c('unauthorised-prompt')],1)}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 345:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_destinationsPage_vue__ = __webpack_require__(147);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_5684226e_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_destinationsPage_vue__ = __webpack_require__(350);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_destinationsPage_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_5684226e_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_destinationsPage_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 346:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_searchDestinations_vue__ = __webpack_require__(148);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_1b9b3842_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_searchDestinations_vue__ = __webpack_require__(347);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_searchDestinations_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_1b9b3842_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_searchDestinations_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 347:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"container"},[_c('h1',{staticClass:"page_title"},[_vm._v("Search Destinations")]),_vm._v(" "),_vm._m(0),_vm._v(" "),_c('b-alert',{attrs:{"variant":"danger","dismissible":""},model:{value:(_vm.showError),callback:function ($$v) {_vm.showError=$$v},expression:"showError"}},[_vm._v(_vm._s(_vm.errorMessage))]),_vm._v(" "),_c('div',[_c('b-form-group',{attrs:{"id":"name-field","label":"Destination Name:","label-for":"name"}},[_c('b-form-input',{attrs:{"id":"name"},model:{value:(_vm.searchName),callback:function ($$v) {_vm.searchName=$$v},expression:"searchName"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"type-field","label":"Destination Type:","label-for":"type"}},[_c('b-form-select',{attrs:{"id":"type","trim":""},model:{value:(_vm.searchType),callback:function ($$v) {_vm.searchType=$$v},expression:"searchType"}},[_c('template',{slot:"first"},[_c('option',{domProps:{"value":null}},[_vm._v("-- Any --")])]),_vm._v(" "),_vm._l((_vm.destinationTypes),function(destination){return _c('option',{domProps:{"value":destination.id}},[_vm._v("\n                    "+_vm._s(destination.destinationType)+"\n                ")])})],2)],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"district-field","label":"District:","label-for":"district"}},[_c('b-form-input',{attrs:{"id":"district","trim":""},model:{value:(_vm.searchDistrict),callback:function ($$v) {_vm.searchDistrict=$$v},expression:"searchDistrict"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"latitude-field","label":"Latitude:","label-for":"latitude"}},[_c('b-form-input',{attrs:{"id":"latitude","trim":""},model:{value:(_vm.searchLat),callback:function ($$v) {_vm.searchLat=$$v},expression:"searchLat"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"longitude-field","label":"Longitude:","label-for":"longitude"}},[_c('b-form-input',{attrs:{"id":"longitude","trim":""},model:{value:(_vm.searchLong),callback:function ($$v) {_vm.searchLong=$$v},expression:"searchLong"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"country-field","label":"Country:","label-for":"country"}},[_c('b-form-input',{attrs:{"id":"country","trim":""},model:{value:(_vm.searchCountry),callback:function ($$v) {_vm.searchCountry=$$v},expression:"searchCountry"}})],1),_vm._v(" "),_c('b-button',{attrs:{"block":"","variant":"primary"},on:{"click":_vm.searchDestinations}},[_vm._v("Search")])],1),_vm._v(" "),_c('div',{staticStyle:{"margin-top":"40px"}},[_c('b-table',{attrs:{"hover":"","striped":"","outlined":"","id":"myFutureTrips","items":_vm.destinations,"fields":_vm.fields,"per-page":_vm.perPage,"current-page":_vm.currentPage,"sort-by":_vm.sortBy,"sort-desc":_vm.sortDesc,"busy":_vm.destinations.length === 0},on:{"update:sortBy":function($event){_vm.sortBy=$event},"update:sort-by":function($event){_vm.sortBy=$event},"update:sortDesc":function($event){_vm.sortDesc=$event},"update:sort-desc":function($event){_vm.sortDesc=$event}}},[_c('div',{staticClass:"text-center my-2",attrs:{"slot":"table-busy"},slot:"table-busy"},[(_vm.retrievingDestinations)?_c('b-spinner',{staticClass:"align-middle"}):_vm._e(),_vm._v(" "),_c('strong',[_vm._v("Can't find any destinations!")])],1)]),_vm._v(" "),_c('b-row',[_c('b-col',{attrs:{"cols":"1"}},[_c('b-form-group',{attrs:{"id":"profiles-field","label-for":"perPage"}},[_c('b-form-select',{attrs:{"id":"perPage","options":_vm.optionViews,"size":"sm","trim":""},model:{value:(_vm.perPage),callback:function ($$v) {_vm.perPage=$$v},expression:"perPage"}})],1)],1),_vm._v(" "),_c('b-col',{attrs:{"cols":"8"}},[_c('b-pagination',{attrs:{"total-rows":_vm.rows,"per-page":_vm.perPage,"aria-controls":"my-table","first-text":"First","last-text":"Last","align":"center","size":"sm"},model:{value:(_vm.currentPage),callback:function ($$v) {_vm.currentPage=$$v},expression:"currentPage"}})],1)],1)],1)],1)}
var staticRenderFns = [function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('p',{staticClass:"page_title"},[_c('i',[_vm._v("Search for a destination using the form below")])])}]
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 348:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_addDestinations_vue__ = __webpack_require__(149);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_a471ec6c_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_addDestinations_vue__ = __webpack_require__(349);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_addDestinations_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_a471ec6c_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_addDestinations_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 349:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"container"},[_c('h1',{staticClass:"page_title"},[_vm._v("Add a Destination")]),_vm._v(" "),_vm._m(0),_vm._v(" "),_c('b-alert',{attrs:{"variant":"danger","dismissible":""},model:{value:(_vm.showError),callback:function ($$v) {_vm.showError=$$v},expression:"showError"}},[_vm._v(_vm._s(_vm.errorMessage))]),_vm._v(" "),_c('b-alert',{attrs:{"show":_vm.dismissCountDown,"dismissible":"","variant":"success"},on:{"dismissed":function($event){_vm.dismissCountDown=0},"dismiss-count-down":_vm.countDownChanged}},[_c('p',[_vm._v("Destination Successfully Added")]),_vm._v(" "),_c('b-progress',{attrs:{"variant":"success","max":_vm.dismissSecs,"value":_vm.dismissCountDown,"height":"4px"}})],1),_vm._v(" "),_c('div',[_c('b-form',[_c('b-form-group',{attrs:{"id":"name-field","label":"Destination Name:","label-for":"dName"}},[_c('b-form-input',{attrs:{"id":"dName","type":"text","required":""},model:{value:(_vm.dName),callback:function ($$v) {_vm.dName=$$v},expression:"dName"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"type-field","label":"Destination Type:","label-for":"type"}},[_c('b-form-select',{attrs:{"id":"type","trim":""},model:{value:(_vm.dType),callback:function ($$v) {_vm.dType=$$v},expression:"dType"}},_vm._l((_vm.destinationTypes),function(destination){return _c('option',{domProps:{"value":destination.id}},[_vm._v("\n                        "+_vm._s(destination.destinationType)+"\n                    ")])}),0)],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"district-field","label":"District:","label-for":"district"}},[_c('b-form-input',{attrs:{"id":"district","type":"text","trim":"","required":""},model:{value:(_vm.dDistrict),callback:function ($$v) {_vm.dDistrict=$$v},expression:"dDistrict"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"latitude-field","label":"Latitude:","label-for":"latitude"}},[_c('b-form-input',{attrs:{"id":"latitude","type":"text","trim":"","required":""},model:{value:(_vm.dLatitude),callback:function ($$v) {_vm.dLatitude=$$v},expression:"dLatitude"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"longitude-field","label":"Longitude:","label-for":"longitude"}},[_c('b-form-input',{attrs:{"id":"longitude","type":"text","trim":"","required":""},model:{value:(_vm.dLongitude),callback:function ($$v) {_vm.dLongitude=$$v},expression:"dLongitude"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"country-field","label":"Country:","label-for":"country"}},[_c('b-form-input',{attrs:{"id":"country","type":"text","trim":"","required":""},model:{value:(_vm.dCountry),callback:function ($$v) {_vm.dCountry=$$v},expression:"dCountry"}})],1),_vm._v(" "),_c('b-button',{attrs:{"block":"","variant":"primary"},on:{"click":_vm.checkDestinationFields}},[_vm._v("Add Destination")])],1)],1)],1)}
var staticRenderFns = [function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('p',{staticClass:"page_title"},[_c('i',[_vm._v("Add a destination using the form below")])])}]
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 350:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return (_vm.profile.length !== 0)?_c('div',[_c('nav-bar-main',{attrs:{"profile":_vm.profile}}),_vm._v(" "),_c('b-navbar',{attrs:{"variant":"light"}},[_c('b-navbar-nav',[_c('b-nav-item',{on:{"click":function($event){return _vm.togglePage(_vm.searchDestinations)}}},[_vm._v("Search for a Destination")]),_vm._v(" "),_c('b-nav-item',{on:{"click":function($event){return _vm.togglePage(_vm.addDestinations)}}},[_vm._v("Add a Destination")])],1)],1),_vm._v(" "),(_vm.searchDestinations)?_c('search-destinations',{attrs:{"profile":_vm.profile,"destinationTypes":_vm.destinationTypes}}):_vm._e(),_vm._v(" "),(_vm.addDestinations)?_c('add-destinations',{attrs:{"profile":_vm.profile,"destinations":_vm.destinations,"destinationTypes":_vm.destinationTypes}}):_vm._e(),_vm._v(" "),_c('footer-main')],1):_c('div',[_c('unauthorised-prompt')],1)}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 351:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_profilesPage_vue__ = __webpack_require__(150);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_3a4da2e8_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_profilesPage_vue__ = __webpack_require__(352);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_profilesPage_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_3a4da2e8_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_profilesPage_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 352:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return (_vm.profile.length !== 0)?_c('div',[_c('nav-bar-main',{attrs:{"profile":_vm.profile}}),_vm._v(" "),_c('div',{staticClass:"container"},[_c('h1',{staticClass:"page_title"},[_vm._v("Find Profiles")]),_vm._v(" "),_vm._m(0),_vm._v(" "),_c('b-alert',{attrs:{"variant":"danger","dismissible":""},model:{value:(_vm.showError),callback:function ($$v) {_vm.showError=$$v},expression:"showError"}},[_vm._v("There's something wrong in the form!")]),_vm._v(" "),_c('div',[_c('b-form-group',{attrs:{"id":"nationalities-field","label":"Nationality:","label-for":"nationality"}},[_c('b-form-select',{attrs:{"id":"nationality","trim":""},model:{value:(_vm.searchNationality),callback:function ($$v) {_vm.searchNationality=$$v},expression:"searchNationality"}},[_c('template',{slot:"first"},[_c('option',{domProps:{"value":null}},[_vm._v("-- Any --")])]),_vm._v(" "),_vm._l((_vm.nationalityOptions),function(nationality){return _c('option',{domProps:{"value":nationality.nationality}},[_vm._v("\n                        "+_vm._s(nationality.nationality)+"\n                    ")])})],2)],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"gender-field","label":"Gender:","label-for":"gender"}},[_c('b-form-select',{attrs:{"id":"gender","options":_vm.genderOptions,"trim":""},model:{value:(_vm.searchGender),callback:function ($$v) {_vm.searchGender=$$v},expression:"searchGender"}},[_c('template',{slot:"first"},[_c('option',{domProps:{"value":null}},[_vm._v("-- Any --")])])],2)],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"minAge-field","label":"Min Age: ","label-for":"minAge"}},[_c('div',{staticClass:"mt-2"},[_vm._v(_vm._s(_vm.searchMinAge))]),_vm._v(" "),_c('b-form-input',{attrs:{"id":"minAge","type":'range',"min":"0","max":"150","trim":""},model:{value:(_vm.searchMinAge),callback:function ($$v) {_vm.searchMinAge=$$v},expression:"searchMinAge"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"maxAge-field","label":"Max Age:","label-for":"maxAge"}},[_c('div',{staticClass:"mt-2"},[_vm._v(_vm._s(_vm.searchMaxAge))]),_vm._v(" "),_c('b-form-input',{attrs:{"id":"maxAge","type":'range',"min":"0","max":"150","trim":""},model:{value:(_vm.searchMaxAge),callback:function ($$v) {_vm.searchMaxAge=$$v},expression:"searchMaxAge"}})],1),_vm._v(" "),_c('b-form-group',{attrs:{"id":"travType-field","label":"Traveller Type:","label-for":"travType"}},[_c('b-form-select',{attrs:{"id":"travType","trim":""},model:{value:(_vm.searchTravType),callback:function ($$v) {_vm.searchTravType=$$v},expression:"searchTravType"}},[[_c('option',{attrs:{"selected":"selected"},domProps:{"value":null}},[_vm._v("-- Any --")])],_vm._v(" "),_vm._l((_vm.travTypeOptions),function(travType){return _c('option',{domProps:{"value":travType.travellerType}},[_vm._v("\n                        "+_vm._s(travType.travellerType)+"\n                    ")])})],2)],1),_vm._v(" "),_c('b-button',{attrs:{"block":"","variant":"primary"},on:{"click":_vm.searchProfiles}},[_vm._v("Search")])],1),_vm._v(" "),_c('div',{staticStyle:{"margin-top":"40px"}},[_c('b-table',{attrs:{"hover":"","striped":"","outlined":"","id":"myFutureTrips","items":_vm.profiles,"fields":_vm.fields,"per-page":_vm.perPage,"current-page":_vm.currentPage,"sort-by":_vm.sortBy,"sort-desc":_vm.sortDesc,"busy":this.profiles.length===0},on:{"update:sortBy":function($event){_vm.sortBy=$event},"update:sort-by":function($event){_vm.sortBy=$event},"update:sortDesc":function($event){_vm.sortDesc=$event},"update:sort-desc":function($event){_vm.sortDesc=$event}},scopedSlots:_vm._u([{key:"actions",fn:function(row){return [_c('b-button',{staticClass:"mr-2",attrs:{"size":"sm"},on:{"click":row.toggleDetails}},[_vm._v("\n                        "+_vm._s(row.detailsShowing ? 'Hide' : 'Show')+" More Details\n                    ")])]}},{key:"row-details",fn:function(row){return [_c('b-card',[_c('view-profile',{attrs:{"profile":row.item}})],1)]}}],null,false,3180400585)},[_c('div',{staticClass:"text-center my-2",attrs:{"slot":"table-busy"},slot:"table-busy"},[(_vm.retrievingProfiles)?_c('b-spinner',{staticClass:"align-middle"}):_vm._e(),_vm._v(" "),_c('strong',[_vm._v("Can't find any profiles!")])],1)]),_vm._v(" "),_c('b-row',[_c('b-col',{attrs:{"cols":"1"}},[_c('b-form-group',{attrs:{"id":"profiles-field","label-for":"perPage"}},[_c('b-form-select',{attrs:{"id":"perPage","options":_vm.optionViews,"size":"sm","trim":""},model:{value:(_vm.perPage),callback:function ($$v) {_vm.perPage=$$v},expression:"perPage"}})],1)],1),_vm._v(" "),_c('b-col',{attrs:{"cols":"8"}},[_c('b-pagination',{attrs:{"total-rows":_vm.rows,"per-page":_vm.perPage,"aria-controls":"my-table","first-text":"First","last-text":"Last","align":"center","size":"sm"},model:{value:(_vm.currentPage),callback:function ($$v) {_vm.currentPage=$$v},expression:"currentPage"}})],1)],1)],1)],1),_vm._v(" "),_c('footer-main')],1):_c('div',[_c('unauthorised-prompt')],1)}
var staticRenderFns = [function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('p',{staticClass:"page_title"},[_c('i',[_vm._v("Search for other travellers using the form below")])])}]
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 353:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_tripsPage_vue__ = __webpack_require__(151);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_7221e8a8_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_tripsPage_vue__ = __webpack_require__(354);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_tripsPage_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_7221e8a8_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_tripsPage_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 354:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return (_vm.profile.length !== 0)?_c('div',[_c('nav-bar-main',{attrs:{"profile":_vm.profile}}),_vm._v(" "),_c('b-navbar',{attrs:{"variant":"light"}},[_c('b-navbar-nav',[_c('b-nav-item',{on:{"click":function($event){return _vm.togglePage(_vm.planATrip)}}},[_vm._v("Plan a Trip")]),_vm._v(" "),_c('b-nav-item',{on:{"click":function($event){return _vm.togglePage(_vm.yourTrips)}}},[_vm._v("Your Trips")])],1)],1),_vm._v(" "),(_vm.planATrip)?_c('plan-a-trip',{attrs:{"destinations":_vm.destinations}}):_vm._e(),_vm._v(" "),(_vm.yourTrips)?_c('your-trips',{attrs:{"profile":_vm.profile}}):_vm._e(),_vm._v(" "),_c('footer-main')],1):_c('div',[_c('unauthorised-prompt')],1)}
var staticRenderFns = []
var esExports = { render: render, staticRenderFns: staticRenderFns }
/* harmony default export */ __webpack_exports__["a"] = (esExports);

/***/ }),

/***/ 355:
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),

/***/ 356:
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),

/***/ 41:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_navbarMain_vue__ = __webpack_require__(144);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_54a50c7c_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_navbarMain_vue__ = __webpack_require__(340);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_navbarMain_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_54a50c7c_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_navbarMain_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 42:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_footerMain_vue__ = __webpack_require__(145);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_f1661d92_hasScoped_true_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_footerMain_vue__ = __webpack_require__(342);
function injectStyle (ssrContext) {
  __webpack_require__(341)
}
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = injectStyle
/* scopeId */
var __vue_scopeId__ = "data-v-f1661d92"
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_footerMain_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_f1661d92_hasScoped_true_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_footerMain_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 43:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_unauthorisedPromptPage_vue__ = __webpack_require__(146);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_c3119c12_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_unauthorisedPromptPage_vue__ = __webpack_require__(343);
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = null
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_unauthorisedPromptPage_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_c3119c12_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_unauthorisedPromptPage_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 69:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__components_index_indexPage_vue__ = __webpack_require__(70);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__assets__ = __webpack_require__(74);
//
//
//
//
//
//
//
//
//




/* harmony default export */ __webpack_exports__["a"] = ({
    computed: {
        assets() {
            return __WEBPACK_IMPORTED_MODULE_1__assets__["a" /* default */];
        }
    },
    mounted() {
        this.getProfile(profile => this.profile = profile);
        this.getNationalities(nationalities => this.nationalityOptions = nationalities);
        this.getTravTypes(travTypes => this.travTypeOptions = travTypes);
        this.getDestinations(destinations => this.destinations = destinations);
        this.getDestinationTypes(destinationT => this.destinationTypes = destinationT);
    },
    data() {
        return {
            profile: "",
            nationalityOptions: [],
            travTypeOptions: [],
            destinations: [],
            destinationTypes: [],
            trips: []
        };
    },
    methods: {
        getDestinations(updateDestinations) {
            return fetch(`/v1/destinations`, {
                accept: "application/json"
            }).then(this.parseJSON).then(updateDestinations);
        },
        getDestinationTypes(updateDestinationTypes) {
            return fetch(`/v1/destinationTypes`, {
                accept: "application/json"
            }).then(this.parseJSON).then(updateDestinationTypes);
        },
        getProfile(updateProfile) {
            return fetch(`/v1/profile`, {
                accept: "application/json"
            }).then(this.parseJSON).then(updateProfile);
        },
        getNationalities(updateNationalities) {
            return fetch(`/v1/nationalities`, {
                accept: "application/json"
            }).then(this.parseJSON).then(updateNationalities);
        },
        getTravTypes(updateTravellerTypes) {
            return fetch(`/v1/travtypes`, {
                accept: "application/json"
            }).then(this.parseJSON).then(updateTravellerTypes);
        },
        parseJSON(response) {
            return response.json();
        }
    },
    components: {
        Index: __WEBPACK_IMPORTED_MODULE_0__components_index_indexPage_vue__["a" /* default */]
    }
});

/***/ }),

/***/ 70:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_indexPage_vue__ = __webpack_require__(71);
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_4b89b976_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_indexPage_vue__ = __webpack_require__(165);
function injectStyle (ssrContext) {
  __webpack_require__(160)
}
var normalizeComponent = __webpack_require__(8)
/* script */


/* template */

/* template functional */
var __vue_template_functional__ = false
/* styles */
var __vue_styles__ = injectStyle
/* scopeId */
var __vue_scopeId__ = null
/* moduleIdentifier (server only) */
var __vue_module_identifier__ = null
var Component = normalizeComponent(
  __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_indexPage_vue__["a" /* default */],
  __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_4b89b976_hasScoped_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_indexPage_vue__["a" /* default */],
  __vue_template_functional__,
  __vue_styles__,
  __vue_scopeId__,
  __vue_module_identifier__
)

/* harmony default export */ __webpack_exports__["a"] = (Component.exports);


/***/ }),

/***/ 71:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__signup_vue__ = __webpack_require__(161);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__login_vue__ = __webpack_require__(163);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//




/* harmony default export */ __webpack_exports__["a"] = ({
    name: "indexPage",
    props: ['profile', 'nationalityOptions', 'travTypeOptions'],
    created() {
        document.title = "Welcome to TravelEA";
    },
    data: function () {
        return {
            username: '',
            password: ''
        };
    },
    mounted() {
        this.getProfile();
    },
    methods: {
        redirectToDash() {
            this.$router.replace("/dash");
        },
        getProfile() {
            let self = this;
            return fetch(`/v1/profile`, {
                accept: "application/json"
            }).then(function (response) {
                if (response.status === 200) {
                    self.redirectToDash();
                }
            });
        }
    },
    components: {
        Signup: __WEBPACK_IMPORTED_MODULE_0__signup_vue__["a" /* default */],
        Login: __WEBPACK_IMPORTED_MODULE_1__login_vue__["a" /* default */]
    }
});

/***/ }),

/***/ 72:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["a"] = ({
    name: "Signup",
    props: ['nationalityOptions', 'travTypeOptions'],
    data: function () {
        return {
            showError: false,
            first_name: '',
            middle_name: '',
            last_name: '',
            email: '',
            password: '',
            rePassword: '',
            dateOfBirth: '',
            gender: '',
            genderOptions: [{ value: 'Male', text: 'Male' }, { value: 'Female', text: 'Female' }, { value: 'Other', text: 'Other' }],
            showFirst: true,
            showSecond: false,
            nationalities: [],
            passports: [],
            travTypes: [],
            validEmail: false
        };
    },
    computed: {
        fNameValidation() {
            if (this.first_name.length === 0) {
                return null;
            }
            let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
            return nameRegex.test(this.first_name);
        },
        mNameValidation() {
            let nameRegex = new RegExp("^(?=.{0,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
            return nameRegex.test(this.middle_name) || this.middle_name.length === 0;
        },
        lNameValidation() {
            if (this.last_name.length === 0) {
                return null;
            }
            let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
            return nameRegex.test(this.last_name);
        },
        emailValidation() {
            if (this.email.length === 0) {
                return null;
            }
            let emailRegex = new RegExp("^([a-zA-Z0-9]+(@)([a-zA-Z]+((.)[a-zA-Z]+)*))(?=.{3,15})");
            this.checkUsername();
            return emailRegex.test(this.email) && this.validEmail;
        },
        passwordValidation() {
            if (this.password.length === 0) {
                return null;
            }
            let passwordRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{5,15})");
            return passwordRegex.test(this.password);
        },
        rePasswordValidation() {
            if (this.rePassword.length === 0) {
                return null;
            }
            return this.password.length > 0 && this.rePassword === this.password;
        },
        dateOfBirthValidation() {
            if (this.dateOfBirth.length === 0) {
                return null;
            }
            return this.dateOfBirth.length > 0;
        },
        genderValidation() {
            if (this.gender.length === 0) {
                return null;
            }
            return this.gender.length > 0;
        },
        nationalityValidation() {
            if (this.nationalities.length === 0) {
                return null;
            }
            return this.nationalities.length > 0;
        },
        passportValidation() {
            if (this.passports.length === 0) {
                return null;
            }
            return this.passports.length > 0;
        },
        travTypeValidation() {
            if (this.travTypes.length === 0) {
                return null;
            }
            return this.travTypes.length > 0;
        }

    },
    methods: {
        checkPersonalForm() {
            if (this.fNameValidation && this.mNameValidation && this.lNameValidation && this.emailValidation && this.passwordValidation && this.rePasswordValidation && this.dateOfBirthValidation && this.genderValidation) {
                this.showError = false;
                this.nextPage();
            } else {
                this.showError = true;
            }
        },
        checkAssociateForm() {
            if (this.nationalityValidation && this.passportValidation && this.travTypeValidation) {
                let profile = {
                    first_name: this.first_name,
                    middle_name: this.middle_name,
                    last_name: this.last_name,
                    username: this.email,
                    password: this.password,
                    date_of_birth: this.dateOfBirth,
                    gender: this.gender,
                    nationality: this.nationalities,
                    passport_country: this.passports,
                    traveller_type: this.travTypes
                };
                this.saveProfile(profile);
            }
        },
        checkUsername() {
            let self = this;
            fetch(`/v1/checkUsername`, {
                method: 'POST',
                headers: { 'content-type': 'application/json' },
                body: JSON.stringify({ 'username': this.email })

            }).then(function (response) {
                self.validEmail = response.ok;
            });
        },
        nextPage() {
            this.showFirst = false;
            this.showSecond = true;
        },
        previousPage() {
            this.showFirst = true;
            this.showSecond = false;
        },

        saveProfile(profile) {
            let self = this;
            fetch('/v1/profiles', {
                method: 'POST',
                headers: { 'content-type': 'application/json' },
                body: JSON.stringify(profile)
            }).then(function (response) {
                self.$router.push("/dash");
                return response.json();
            });
        },
        parseJSON(response) {
            return response.json();
        }
    }
});

/***/ }),

/***/ 73:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["a"] = ({
    name: "login",
    data: function () {
        return {
            username: '',
            password: '',
            showError: false
        };
    },
    computed: {},
    methods: {
        login() {
            let self = this;
            fetch('/v1/login', {
                method: 'POST',
                headers: { 'content-type': 'application/json' },
                body: JSON.stringify({ username: this.username, password: this.password })
            }).then(function (response) {
                if (response.ok) {
                    self.showError = false;
                    self.$router.go();
                    return JSON.parse(JSON.stringify(response));
                } else {
                    self.showError = true;
                    return JSON.parse(JSON.stringify(response));
                }
            });
        },
        parseJSON(response) {
            return response.json();
        }
    }
});

/***/ }),

/***/ 74:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony default export */ __webpack_exports__["a"] = ({
    appLogo: './static/full_logo_sm.png',
    baumans: './static/Baumans-Regular.ttf',
    background: './static/background_image.jpg'
});

/***/ })

},[153]);
//# sourceMappingURL=app.js.map
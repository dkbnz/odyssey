<template>
    <div>
        <div class="bg-white m-2 pt-3 pl-3 pr-3 pb-3 rounded-lg">
            <b-alert v-model="showError">{{alertMessage}}</b-alert>
            <!-- Confirmation modal for deleting a profile. -->
            <b-modal ref="deleteProfileModal" id="deleteProfileModal" hide-footer title="Delete Profile">
                <div class="d-block">
                    Are you sure that you want to delete "{{selectedProfile.firstName}} {{selectedProfile.lastName}}"?
                </div>
                <b-button
                        @click="dismissModal()
                         deleteUser(selectedProfile);"
                        class="mr-2 float-right"
                        variant="danger">Delete
                </b-button>
                <b-button
                        @click="dismissModal()"
                        class="mr-2 float-right">Cancel
                </b-button>
            </b-modal>

            <h1 class="page-title">Leaderboard</h1>
            <p class="page-title"><i>Find other players using any of the fields below!</i></p>

            <b-row style="margin-top: 40px" v-if="!minimalInfo">
                <b-col sm="4">
                    <h3 class="page-title show-only-desktop">Search for Players</h3>
                    <profile-search-form
                            :userProfile="profile"
                            @search="searchProfiles"
                            @cleared-form="clearForm">
                    </profile-search-form>
                </b-col>
                <b-col sm="8">
                    <!--Displays results from profile search in a table format-->
                    <profile-list
                            :first-page = "firstPage"
                            :more-results="moreResults"
                            :searching-profiles="searchingProfiles"
                            :search-parameters="searchParameters"
                            :profile-list="profiles"
                            :profile="profile"
                            :userProfile="profile"
                            :loading="retrievingProfiles"
                            :admin-view="minimalInfo"
                            @make-admin="makeAdmin"
                            @remove-admin="removeAdmin"
                            @admin-edit="profileEdited => $emit('admin-edit', profileEdited)"
                            @profile-delete="sendProfileToModal"
                            @get-more="getMore"
                            @clear-profiles="clearProfiles"
                            @sort-table="sortTable"
                    >
                    </profile-list>
                </b-col>
            </b-row>
            <div v-else>
                <profile-search-form
                        :userProfile="profile"
                        @search="searchProfiles"
                        @cleared-form="clearForm">
                </profile-search-form>
                <profile-list
                        :firstPage = "firstPage"
                        :more-results="moreResults"
                        :searching-profiles="searchingProfiles"
                        :search-parameters="searchParameters"
                        :profile-list="profiles"
                        :profile="profile"
                        :userProfile="profile"
                        :loading="retrievingProfiles"
                        :admin-view="minimalInfo"
                        @make-admin="makeAdmin"
                        @remove-admin="removeAdmin"
                        @admin-edit="profileEdited => $emit('admin-edit', profileEdited)"
                        @profile-delete="sendProfileToModal"
                        @get-more="getMore"
                        @clear-profiles="this.profiles = []"
                        @sort-table="sortTable"
                >
                </profile-list>
            </div>
        </div>
        <footer-main></footer-main>
    </div>
</template>

<script>
    import ViewProfile from '../dash/viewProfile.vue'
    import Dash from '../dash/dashPage'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'
    import ProfileList from "./profileList";
    import ProfileSearchForm from "./profileSearchForm";

    export default {
        name: "desktopLeaderboard",

        props: {
            profile: Object,
            minimalInfo: {
                default: function () {
                    return false;
                }
            },
            destinations: Array,
            destinationTypes: Array
        },

        components: {
            ProfileSearchForm,
            ProfileList,
            ViewProfile,
            NavBarMain,
            FooterMain,
            Dash,
            UnauthorisedPrompt
        },

        data: function () {
            return {
                showError: false,
                alertMessage: "",
                profiles: [],
                retrievingProfiles: false,
                selectedProfile: "",
                queryPage: 0,
                pageSize: 5,
                moreResults: true,
                gettingMore: false,
                searchParameters: null,
                searchingProfiles: false,
                columnSortBy: {sortBy: "", order: ""},
                firstPage: false
            }
        },

        mounted() {
            this.queryProfiles();
        },

        methods: {
            /**
             * Used to calculate a specific rows nationalities from their list of nationalities. Shows all the
             * nationalities in the row.
             *
             * @param nationalities     the row's (profile) nationalities.
             */
            calculateNationalities(nationalities) {
                let nationalityList = "";
                for (let i = 0; i < nationalities.length; i++) {
                    if (nationalities[i + 1] !== undefined) {
                        nationalityList += nationalities[i].nationality + ", ";
                    } else {
                        nationalityList += nationalities[i].nationality;
                    }

                }
                return nationalityList;
            },


            /**
             * Used to calculate a specific rows traveller types from their list of traveller types. Shows all the
             * traveller types in the row.
             *
             * @param travellerTypes     the row's (profile) traveller types.
             */
            calculateTravTypes(travellerTypes) {
                let travTypeList = "";
                for (let i = 0; i < travellerTypes.length; i++) {
                    if (travellerTypes[i + 1] !== undefined) {
                        travTypeList += travellerTypes[i].travellerType + ", ";
                    } else {
                        travTypeList += travellerTypes[i].travellerType;
                    }

                }
                return travTypeList;
            },


            /**
             * Method to make a user an admin. This method is only available if the currently logged in user is an
             * admin. Backend validation ensures a user cannot bypass this.
             *
             * @param makeAdminProfile      the selected profile to be made an admin.
             */
            makeAdmin(makeAdminProfile) {
                let self = this;
                fetch('/v1/makeAdmin/' + makeAdminProfile.id, {
                    method: 'POST',
                }).then(function () {
                    self.searchProfiles();
                })
            },


            /**
             * Method to remove admin attribute from a user. This method is only available if the currently logged in
             * user is an admin. Backend validation ensures a user cannot bypass this.
             *
             * @param makeAdminProfile      the selected profile to be removed as an admin.
             */
            removeAdmin(makeAdminProfile) {
                let self = this;
                fetch('/v1/removeAdmin/' + makeAdminProfile.id, {
                    method: 'POST',
                }).then(function () {
                    self.searchProfiles();
                }).then(function () {
                    if (self.profile.id === makeAdminProfile.id) {
                        self.$router.push("/dash");
                        self.$router.go();
                    }
                })
            },


            /**
             * Method to delete a user's profile. This method is only available if the currently logged in
             * user is an admin. Backend validation ensures a user cannot bypass this.
             *
             * @param deleteUser      the selected profile to be deleted.
             */
            deleteUser(deleteUser) {
                let self = this;
                fetch('/v1/profile/' + deleteUser.id, {
                    method: 'DELETE',
                }).then(function () {
                    self.searchProfiles();
                })
            },


            /**
             * Function to retrieve more destinations when a user reaches the bottom of the list.
             */
            getMore(queryPage, pageSize) {
                //TODO: Isaac - Still some bugs in this section.
                if (pageSize !== this.pageSize) {
                    this.profiles = [];
                    this.pageSize = pageSize;
                    this.queryProfiles();
                } else {
                    console.log("QUERY PAGE CATCHUP");
                    this.queryPage = queryPage;
                    // while (this.queryPage < queryPage) {
                    //     this.queryPage += 1;
                        this.queryProfiles();
                    // }
                }
                if (this.pageSize === Infinity) {
                    console.log("INFINITY");
                    this.queryPage = 0;
                    this.queryProfiles();
                }

                this.gettingMore = true;
            },


            /**
             * Used to initialise the appropriate parameters for searching for profiles.
             */
            searchProfiles(searchParameters) {
                this.queryPage = 0;
                this.searchParameters = searchParameters;
                this.queryProfiles();
            },


            sortTable(columnSortBy) {
                this.columnSortBy = columnSortBy;
                this.queryPage = 0;
                this.queryProfiles();
                this.firstPage = !this.firstPage;
            },


            /**
             * Queries database for profiles which fit search criteria.
             */
            queryProfiles() {
                this.retrievingProfiles = true;
                let searchQuery = "";
                if (!this.searchParameters) {
                    searchQuery =
                        "?name=" + "" +
                        "&nationalities=" + "" +
                        "&gender=" + "" +
                        "&min_age=" + "" +
                        "&max_age=" + "" +
                        "&travellerTypes=" + "" +
                        "&rank=" +
                        "&page=" + this.queryPage +
                        "&pageSize=" + this.pageSize +
                        "&sortBy=" + this.columnSortBy.sortBy +
                        "&sortOrder=" + this.columnSortBy.order;
                    this.searchingProfiles = false;
                } else {
                    this.gettingMore = false;
                    searchQuery =
                        "?name=" + this.searchParameters.name +
                        "&nationalities=" + this.searchParameters.nationality +
                        "&gender=" + this.searchParameters.gender +
                        "&min_age=" + this.searchParameters.age[0] +
                        "&max_age=" + this.searchParameters.age[1] +
                        "&travellerTypes=" + this.searchParameters.travellerType +
                        "&rank=" + this.searchParameters.rank +
                        "&page=" + this.queryPage +
                        "&pageSize=" + this.pageSize +
                        "&sortBy=" + this.columnSortBy.sortBy +
                        "&sortOrder=" + this.columnSortBy.order;
                    this.searchingProfiles = true;
                }
                return fetch(`/v1/profiles` +  searchQuery, {
                    method: "GET"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.profiles = data;
                        this.retrievingProfiles = false;
                    })
            },


            /**
             * Used to check the response of a fetch method. If there is an error code, the code is printed to the
             * console.
             *
             * @param response, passed back to the getAllTrips function to be parsed into a Json.
             * @returns throws the error.
             */
            checkStatus(response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                this.showError = true;
                response.clone().text().then(text => {
                    this.alertMessage = text;
                });
                throw error;
            },


            /**
             * Used to turn the response of the fetch method into a usable Json.
             *
             * @param response of the fetch method.
             * @returns the Json body of the response.
             */
            parseJSON(response) {
                return response.json();
            },


            /**
             * Used to send a selected profile to a modal so the admin can confirm they want to delete the selected
             * profile.
             *
             * @param profile, the profile that is selected to be deleted.
             */
            sendProfileToModal(profile) {
                this.selectedProfile = profile;
            },


            /**
             * Used to dismiss the delete a profile modal.
             */
            dismissModal() {
                this.$refs['deleteProfileModal'].hide();
            },


            /**
             * Emits the event that the admin is viewing a profile to edit, this will navigate to the edit page.
             */
            emitAdminEdit(profile) {
                this.$emit('admin-edit', profile);
            },


            /**
             * Clears the relevant fields when the clear form button is clicked.
             */
            clearForm() {
                this.queryPage = 0;
                this.firstPage = !this.firstPage;
                this.columnSortBy = {sortBy: "", order: ""};
                this.searchingProfiles = false;
                this.searchParameters = null;
                this.profiles = [];
                this.queryProfiles();
            },


            /**
             * Clears the list of profiles, this only occurs when the per page value is changed in the profiles table.
             */
            clearProfiles() {
                this.profiles = [];
            }

        }
    }
</script>
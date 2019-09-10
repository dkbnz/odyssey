<template>
    <div>
        <b-alert v-model="showError" variant="danger" dismissible><p class="errorMessage">{{alertMessage}}</p></b-alert>
        <div v-if="page === 0">
            <b-jumbotron class="bg-white">
                <h1 class="page-title">Leaderboard</h1>
                <p class="page-title"><i>Find other players using any of the fields below!</i></p>
                <profile-search-form
                        :userProfile="profile"
                        @search="queryProfiles">
                </profile-search-form>
            </b-jumbotron>
        </div>
        <div v-if="page === 1">
            <b-button @click="page = 0" block variant="primary">Search</b-button>
            <div class="d-flex justify-content-center mb-3 buttonMarginsTop">
                <b-img alt="Loading" v-if="retrievingProfiles && initialLoad" class="align-middle loading" :src="assets['loadingLogo']"></b-img>
            </div>
            <mobile-profile-list
                    :loading="retrievingProfiles"
                    :profile-list="profiles"
                    @profile-click="selectProfile"
            >
            </mobile-profile-list>

            <div class="text-center my-2" v-if="profiles.length === 0 && !retrievingProfiles">
                <strong>Can't find any profiles!</strong>
            </div>
            <div class="flex-column justify-content-center">
                <div class="d-flex justify-content-center">
                    <b-img alt="Loading" v-if="retrievingProfiles && !initialLoad" class="loading"
                           :src="assets['loadingLogo']">
                    </b-img>
                </div>
                <div v-if="!retrievingProfiles && profiles.length > 0">
                    <div v-if="moreResults">
                        <b-button variant="success" @click="getMore" block>More</b-button>
                    </div>
                    <div v-else class="d-flex justify-content-center">
                        <h5 class="mb-1">No More Results</h5>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="page === 2">
            <b-button @click="page = 1" block variant="primary">Return to List</b-button>
            <view-profile
                    :profile="selectedProfile"
                    :user-profile="profile"
            ></view-profile>
        </div>
    </div>
</template>

<script>
    import ProfileSearchForm from "./profileSearchForm";
    import ProfileList from "./profileList";
    import MobileProfileList from "./mobileProfileList";
    import SingleProfile from "../admin/singleProfile";
    import ViewProfile from "../dash/viewProfile";
    export default {
        name: "mobileLeaderboard",

        components: {
            ViewProfile,
            SingleProfile,
            MobileProfileList,
            ProfileList,
            ProfileSearchForm
        },

        props: {
            profile: Object
        },

        watch: {
            page() {
                if (this.page === 0) {
                    this.queryPage = 0;
                    this.profiles = [];
                }

            }
        },

        data() {
            return {
                page: 0,
                retrievingProfiles: false,
                profiles: [],
                selectedProfile: {},
                moreResults: true,
                initialLoad: true,
                queryPage: 0,
                pageSize: 10,
                gettingMore: false,
                searchParameters: null,
                showError: false,
                alertMessage: ""
            }
        },

        methods: {
            /**
             * Function to retrieve more destinations when a user reaches the bottom of the list.
             */
            getMore() {
                this.queryPage += 1;
                this.queryProfiles(this.searchParameters);
                this.gettingMore = true;
            },


            /**
             * Queries database for profiles which fit search criteria.
             */
            queryProfiles(searchParameters) {
                this.retrievingProfiles = true;
                this.searchParameters = searchParameters;
                let self = this;
                let searchQuery = "";
                this.page = 1;
                if (!this.searchParameters) {
                    searchQuery =
                        "?name=" + "" +
                        "&nationalities=" + "" +
                        "&gender=" + "" +
                        "&min_age=" + "" +
                        "&max_age=" + "" +
                        "&travellerTypes=" + "" +
                        "&page=" + this.queryPage +
                        "&pageSize=" + this.pageSize;
                } else {
                    searchQuery =
                        "?name=" + this.searchParameters.name +
                        "&nationalities=" + this.searchParameters.nationality +
                        "&gender=" + this.searchParameters.gender +
                        "&min_age=" + this.searchParameters.age[0] +
                        "&max_age=" + this.searchParameters.age[1] +
                        "&travellerTypes=" + this.searchParameters.travellerType +
                        "&page=" + this.queryPage +
                        "&pageSize=" + this.pageSize;
                }

                return fetch(`/v1/profiles` + searchQuery, {})
                    .then(function (response) {
                        response.json().then(data => {
                            if (response.ok && data !== null && data !== undefined) {
                                self.showError = false;
                                if (data.length < 10) {
                                    self.moreResults = false;
                                    self.initialLoad = false;
                                } else {
                                    self.moreResults = true;
                                    self.initialLoad = false;
                                }
                                if (!this.gettingMore && data.length === 0) {
                                    self.profiles = [];
                                }
                                for (let i = 0; i < data.length; i++) {
                                    if (self.gettingMore) {
                                        self.profiles.push(data[i]);
                                    } else {
                                        self.gettingMore = false;
                                        self.profiles = data;
                                    }
                                }
                                self.retrievingProfiles = false;
                            } else {
                                self.alertMessage = self.getErrorMessage(data);
                                self.showError = true;
                            }
                        });
                    });
            },

            selectProfile(profile) {
                this.selectedProfile = profile;
                this.page = 2
            }
        }
    }
</script>
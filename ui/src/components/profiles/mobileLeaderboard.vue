<template>
    <div>
        <div v-if="page === 0">
            <b-jumbotron class="search">
                <h1 class="page-title">Leaderboard</h1>
                <p class="page-title"><i>Find other players using any of the fields below</i></p>
                <profile-search-form
                        :userProfile="profile"
                        @search="queryProfiles">
                </profile-search-form>
            </b-jumbotron>
        </div>
        <div v-if="page === 1">
            <b-button @click="page = 0" block variant="primary">Search</b-button>
            <div class="d-flex justify-content-center mb-3 buttonMarginsTop">
                <b-img alt="Loading" v-if="retrievingProfiles && initialLoad" class="align-middle loading" src="../../../static/logo.png" width="50%"></b-img>
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
                <div class="d-flex justify-content-center" v-if="retrievingProfiles && !initialLoad">
                    <b-img alt="Loading" class="align-middle loading" src="../../../static/logo.png" width="50%"></b-img>
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
                gettingMore: false,
                searchParameters: {}
            }
        },

        methods: {
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
                console.log(error); // eslint-disable-line no-console
                throw error;
            },


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
                let searchQuery = "";
                this.page = 1;
                if (searchParameters !== undefined) {
                    searchQuery =
                        "?nationalities=" + searchParameters.nationality +
                        "&gender=" + searchParameters.gender +
                        "&min_age=" + searchParameters.age[0] +
                        "&max_age=" + searchParameters.age[1] +
                        "&travellerTypes=" + searchParameters.travellerType +
                        "&page=" + this.queryPage;
                } else {
                    searchQuery =
                        "?name=" + "" +
                        "&nationalities=" + "" +
                        "&gender=" + "" +
                        "&min_age=" + "" +
                        "&max_age=" + "" +
                        "&travellerTypes=" + "" +
                        "&page=" + this.queryPage;
                }

                return fetch(`/v1/profiles` + searchQuery, {})
                    .then(this.checkStatus)
                    .then(response => response.json())
                    .then((data) => {
                        if (data === undefined || data.length < 10) {
                            this.moreResults = false;
                            this.initialLoad = false;
                        } else {
                            this.moreResults = true;
                            this.initialLoad = false;
                        }
                        if (!this.gettingMore && data.length === 0) {
                            this.profiles = [];
                        }
                        for (var i = 0; i < data.length; i++) {
                            if (this.gettingMore) {
                                this.profiles.push(data[i]);
                            } else {
                                this.gettingMore = false;
                                this.profiles = data;
                            }
                        }
                        this.retrievingProfiles = false;
                    })
            },

            selectProfile(profile) {
                this.selectedProfile = profile;
                this.page = 2
            }
        }
    }
</script>

<style scoped>
    .search {
        background-color: white;
    }
</style>
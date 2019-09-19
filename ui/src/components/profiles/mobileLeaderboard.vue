<template>
    <div>
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
            <b-button @click="page = 0" class="mt-2" block variant="primary">Return to Search</b-button>
            <mobile-profile-list
                    :loading="retrievingProfiles"
                    :profile-list="profiles"
                    @profile-click="selectProfile"
            >
            </mobile-profile-list>

            <div class="text-center" v-if="profiles.length === 0 && !retrievingProfiles">
                <strong>Can't find any profiles!</strong>
            </div>
            <div class="flex-column justify-content-center" v-else>
                <div class="d-flex justify-content-center" v-if="retrievingProfiles">
                    <b-img alt="Loading" class="loading"
                           :src="assets['loadingLogo']">
                    </b-img>
                </div>
                <div class="mt-2" v-else>
                    <div v-if="moreResults">
                        <b-button variant="success" @click="getMore" block>Load More</b-button>
                    </div>
                    <div v-else class="d-flex justify-content-center">
                        <h5 class="mb-1">No More Results</h5>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="page === 2">
            <b-button @click="page = 1" block variant="primary" class="mt-2">Return to List</b-button>
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
                searchParameters: null,
                queryPage: 0,
                pageSize: 50,
            }
        },

        methods: {
            /**
             * Function to retrieve more destinations when a user reaches the bottom of the list.
             */
            getMore() {
                this.queryPage += 1;
                this.queryProfiles(this.searchParameters);
            },


            /**
             * Queries database for profiles which fit search criteria.
             *
             * @param searchParameters  the search parameters that generate the search query for profiles.
             * @return {Promise <Response | never>}        the fetch method to retrieve profiles.
             */
            queryProfiles(searchParameters) {
                this.searchParameters = searchParameters;
                this.retrievingProfiles = true;
                this.page = 1;
                let searchQuery =
                        "?name=" + this.searchParameters.name +
                        "&nationalities=" + this.searchParameters.nationality +
                        "&gender=" + this.searchParameters.gender +
                        "&min_age=" + this.searchParameters.age[0] +
                        "&max_age=" + this.searchParameters.age[1] +
                        "&travellerTypes=" + this.searchParameters.travellerType +
                        "&page=" + this.queryPage +
                        "&pageSize=" + this.pageSize;

                let self = this;
                fetch(`/v1/profiles` + searchQuery, {})
                    .then(function (response) {
                        if (!response.ok) {
                            throw response;
                        } else {
                            return response.json();
                        }
                    }).then(function (responseBody) {
                        // If the response body is of size 50, we are not at the last page, more results available.
                        self.moreResults = (responseBody.length === 50);

                        for (let i = 0; i < responseBody.length; i++) {
                            self.profiles.push(responseBody[i]);
                        }
                    }).catch(function (response) {
                        if (response.status > 404) {
                            self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                        } else {
                            response.json().then(function(responseBody) {
                                self.showErrorToast(responseBody);
                            });
                        }
                    });

                this.retrievingProfiles = false;
            },


            /**
             * Displays the view full profile page for the mobile.
             *
             * @param profile   the profile to be displayed in full.
             */
            selectProfile(profile) {
                this.selectedProfile = profile;
                this.page = 2
            }
        }
    }
</script>
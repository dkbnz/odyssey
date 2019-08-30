<template>
    <div>
        <div v-if="page === 0">
            <b-jumbotron class="search">
                <h1 class="page-title">Leaderboard</h1>
                <p class="page-title"><i>Find other players using any of the fields below</i></p>
                <profile-search-form @search="queryProfiles"></profile-search-form>
            </b-jumbotron>
        </div>
        <div v-if="page === 1">
            <b-button @click="page = 0" block variant="primary">Search</b-button>
            <mobile-profile-list
                    :loading="retrievingProfiles"
                    :profile-list="profiles"
                    @profile-click="selectProfile"
            >
            </mobile-profile-list>
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
        components: {ViewProfile, SingleProfile, MobileProfileList, ProfileList, ProfileSearchForm},
        props: {
            profile: Object
        },
        data() {
            return {
                page: 0,
                retrievingProfiles: false,
                profiles: [],
                selectedProfile: {}
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
             * Queries database for profiles which fit search criteria.
             */
            queryProfiles(searchParameters) {
                this.retrievingProfiles = true;
                this.page = 1;
                let searchQuery = "";
                if (searchParameters !== undefined) {
                    searchQuery =
                        "?nationalities=" + searchParameters.nationality +
                        "&gender=" + searchParameters.gender +
                        "&min_age=" + searchParameters.minAge +
                        "&max_age=" + searchParameters.maxAge +
                        "&travellerTypes=" + searchParameters.travellerType;
                }

                return fetch(`/v1/profiles` + searchQuery, {})
                    .then(this.checkStatus)
                    .then(response => response.json())
                    .then((data) => {
                        this.retrievingProfiles = false;
                        this.profiles = data;
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
<template>
    <div>
        <div v-if="shownDiv === 0">
            <b-list-group>
                <b-list-group-item class="flex-column align-items-start">
                    <search-destinations v-if="shownDiv === 0"
                                         :profile="profile"
                                         :destinationTypes="destinationTypes"
                                         @searched-destination="initialQuery"
                    ></search-destinations>
                </b-list-group-item>
            </b-list-group>
        </div>
        <div class="d-flex justify-content-center mb-3" v-if="shownDiv === 1">
            <b-spinner label="Loading..."></b-spinner>
        </div>
        <div v-if="shownDiv === 2">
            <b-button variant="success" @click="shownDiv = 0" block>Search</b-button>
            <b-list-group class="scroll">
                <b-list-group-item v-for="destination in (publicDestinations)" href="#" class="flex-column align-items-start" @click="$emit('destination-click', destination)">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">{{destination.name}}</h5>
                    </div>

                    <p class="mb-1">
                        {{destination.district}}
                    </p>
                    <p class="mb-1">
                        {{destination.country}}
                    </p>

                </b-list-group-item>
                <b-list-group-item href="#" class="flex-column justify-content-center">
                    <div v-if="moreResults">
                        <b-button variant="success" @click="getMore" block>More</b-button>
                        <!--<b-spinner label="Loading..."></b-spinner>-->
                    </div>
                    <div v-else>
                        <p class="mb-1">
                            No More Results
                        </p>
                    </div>
                </b-list-group-item>
            </b-list-group>
        </div>
</div>
</template>

<script>
import SearchDestinations from "./searchDestinations";
export default {
   name: "publicDestinations",
   components: {SearchDestinations},
   props: ['profile', 'destinationTypes'],
    data() {
       return {
           publicDestinations: [],
           shownDiv: 0, //0 - Search, 1 - Loading, 2 - Listing
           moreResults: true,
           destToSearch: {},
           queryPage: 0
       }
    },
    methods: {

       getMore() {
           this.queryPage += 1;
           this.queryDestinations(this.destToSearch);
       },

       initialQuery(destinationToSearch) {
           this.shownDiv = 1;
           this.publicDestinations = [];
           this.destToSearch = destinationToSearch;
           this.queryDestinations(this.destToSearch);
           this.shownDiv = 2;
       },
        /**
         * Runs a query which searches through the destinations in the database and returns all which
         * follow the search criteria.
         *
         * @returns {Promise<Response | never>}
         */
        queryDestinations(destinationToSearch) {
            let searchQuery =
                "?name=" + destinationToSearch.name +
                "&type_id=" + destinationToSearch.type +
                "&district=" + destinationToSearch.district +
                "&latitude=" + destinationToSearch.latitude +
                "&longitude=" + destinationToSearch.longitude +
                "&country=" + destinationToSearch.country +
                "&page=" + this.queryPage;

            return fetch(`/v1/destinations` + searchQuery, {
                dataType: 'html'
            })
                .then(this.checkStatus)
                .then(this.parseJSON)
                .then((data) => {
                    if (data === undefined || data.length == 0) {
                        this.moreResults = false;
                    }
                    for (var i = 0; i < data.length; i++) {
                        this.publicDestinations.push(data[i]);
                    }
                })
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


}
</script>

<style scoped>

</style>
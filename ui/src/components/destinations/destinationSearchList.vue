<template>
    <div>
        <div v-if="showSearch">
            <b-list-group>
                <b-list-group-item class="flex-column align-items-start">
                    <search-destinations v-if="showSearch"
                                         :search-public="searchPublic"
                                         :profile="profile"
                                         :destinationTypes="destinationTypes"
                                         @searched-destination="initialQuery"
                    ></search-destinations>
                </b-list-group-item>
            </b-list-group>
        </div>
        <div v-else>
            <b-button variant="success" @click="showSearch = true" block>Search</b-button>
            <b-list-group class="scroll">
                <b-list-group-item v-for="destination in foundDestinations" href="#"
                                   class="flex-column align-items-start"
                                   @click="$emit('destination-click', destination)" :key="destination.id">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">{{destination.name}}</h5>

                        <div v-if="!searchPublic">
                            <small v-if="destination.public">Public</small>
                            <small v-else>Private</small>
                        </div>
                    </div>

                    <p class="mb-1">
                        {{destination.district}}
                    </p>
                    <p class="mb-1">
                        {{destination.country}}
                    </p>

                </b-list-group-item>
                <b-list-group-item href="#" class="flex-column justify-content-center">
                    <div class="d-flex justify-content-center" v-if="loadingResults">
                        <b-img alt="Loading" class="align-middle loading" src="../../../static/logo.png" width="50%"></b-img>
                    </div>
                    <div>
                        <div v-if="moreResults && !loadingResults">
                            <b-button variant="success" class="buttonMarginsTop" @click="getMore" block>More</b-button>
                        </div>
                        <div class="d-flex justify-content-center" v-else-if="!moreResults && !loadingResults">
                            <h5 class="mb-1">No More Results</h5>
                        </div>
                    </div>

                </b-list-group-item>
            </b-list-group>
        </div>
</div>
</template>

<script>
    import SearchDestinations from "./searchDestinationForm";
    export default {
        name: "destinationSearchList",

        components: {SearchDestinations},

        props: {
            profile: {
                default: function () {
                    return null;
                }
            },
            destinationTypes: Array,
            searchPublic: Boolean
        },

        data() {
           return {
               foundDestinations: [],
               showSearch: true,
               loadingResults: false,
               moreResults: true,
               destToSearch: {},
               queryPage: 0
           }
        },

        methods: {
           /**
            * Function to retrieve more destinations when a user reaches the bottom of the list.
            */
           getMore() {
               this.queryPage += 1;
               this.queryDestinations(this.destToSearch);
           },


            /**
             * Resets the data fields and performs the initial search query when a user clicks search
             */
           initialQuery(destinationToSearch) {
               this.showSearch = false;
               this.queryPage = 0;
               this.foundDestinations = [];
               this.destToSearch = destinationToSearch;
               this.queryDestinations(this.destToSearch);
           },


            /**
             * Runs a query which searches through the destinations in the database and returns all which
             * follow the search criteria.
             *
             * @returns {Promise<Response | never>}
             */
            queryDestinations(destinationToSearch) {
                this.loadingResults = true;
                let searchQuery =
                    "?name=" + destinationToSearch.name +
                    "&type_id=" + destinationToSearch.type +
                    "&district=" + destinationToSearch.district +
                    "&latitude=" + destinationToSearch.latitude +
                    "&longitude=" + destinationToSearch.longitude +
                    "&country=" + destinationToSearch.country +
                    (this.searchPublic ? "&is_public=1" : "&owner=" + this.profile.id) +
                    "&page=" + this.queryPage;

                return fetch(`/v1/destinations` + searchQuery, {
                    dataType: 'html'
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        if (data !== null && data !== undefined) {
                            this.moreResults = data.length >= 50;
                            for (let i = 0; i < data.length; i++) {
                                this.foundDestinations.push(data[i]);
                            }
                            this.$emit('destination-search', this.foundDestinations);
                            this.loadingResults = false;
                        }

                    })
            },


            /**
             * Checks the Http response for errors.
             *
             * @param response the retrieved Http response.
             * @returns {*} throws the Http response error.
             */
            checkStatus(response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                throw error;
            },


            /**
             * Converts the Http response body to a Json.
             * @param response  the received Http response.
             * @returns {*}     the response body as a Json object.
             */
            parseJSON(response) {
                return response.json();
            }
        }
    }
</script>
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
            <b-alert v-model="showError" variant="danger" dismissible>{{errorMessage}}</b-alert>
            <b-button variant="success" @click="showSearch = true" block>Back to Search</b-button>
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
                        <b-img alt="Loading" class="align-middle loading" :src="assets['loadingLogo']"></b-img>
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
               queryPage: 0,
               showError: false,
               errorMessage: ""
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
                let self = this;
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
                }).then(function(response) {
                    response.json().then(data => {
                        if (response.ok) {
                            if (data !== null && data !== undefined) {
                                self.showError = false;
                                self.moreResults = data.length >= 50;
                                for (let i = 0; i < data.length; i++) {
                                    self.foundDestinations.push(data[i]);
                                }
                                self.$emit('destination-search', self.foundDestinations);
                            }
                        } else {
                            self.errorMessage = self.getErrorMessage(data);
                            self.showError = true;
                        }
                        self.loadingResults = false;
                    })
                });
            }
        }
    }
</script>
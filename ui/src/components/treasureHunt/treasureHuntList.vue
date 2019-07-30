<template>
    <div>
        <b-list-group class="scroll">
            <b-list-group-item v-for="treasureHunt in (foundTreasureHunts)" href="#"
                               class="flex-column align-items-start"
                               :key="treasureHunt.id">
                <template v-if="!editingHunt" >
                        {{treasureHunt.riddle}}
                        {{treasureHunt.startDate}}
                        {{treasureHunt.endDate}}

                    <b-row>
                        <b-col>
                            <b-button variant="warning" @click="editingHunt=true" block>Edit</b-button>
                        </b-col>
                        <b-col>
                            <b-button variant="danger" @click="deleteTreasureHunt(treasureHunt.id)" block>Delete</b-button>
                        </b-col>
                    </b-row>
                </template>
                <add-treasure-hunt v-else
                                   :profile="profile"
                                   :heading="'Edit'"
                                   :input-treasure-hunt="treasureHunt"
                                   @cancelCreate="editingHunt=false">

                </add-treasure-hunt>
                <!--Treasure Hunt component-->
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="loadingResults">
                <div class="d-flex justify-content-center" >
                    <b-spinner label="Loading..."></b-spinner>
                </div>
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="!loadingResults && foundTreasureHunts.length === 0">
                <div class="d-flex justify-content-center">
                    <strong>No Treasure Hunts</strong>
                </div>
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="creatingHunt">
                <add-treasure-hunt :profile="profile" :heading="'Create'" @cancelCreate="creatingHunt=false">

                </add-treasure-hunt>
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center">
                <div class="d-flex justify-content-center" >
                    <b-button variant="success" @click="addTreasureHunt" block>Add</b-button>
                </div>
            </b-list-group-item>
        </b-list-group>
    </div>
</template>

<script>
    import AddTreasureHunt from "./addTreasureHunt";

    export default {
        name: "treasureHuntList",

        props: {
            profile: Object,
            adminView: {
                default: function() {
                    return false;
                }
            },
            yourTreasureHunts: Boolean
        },

        data() {
            return {
                foundTreasureHunts: [],
                loadingResults: true,
                moreResults: true,
                queryPage: 0,
                creatingHunt: false,
                editingHunt: false
            }
        },

        mounted() {
            this.getMore();
        },

        methods: {
            /**
             * Function to retrieve more treasure hunts when a user reaches the bottom of the list.
             */
            getMore() {
                this.queryPage += 1;
                if (this.yourTreasureHunts) {
                    this.queryYourTreasureHunts(this.profile);
                } else {
                    this.queryTreasureHunts();
                }
            },


            /**
             * Send the Http request to delete the specified treasure hunt.
             */
            deleteTreasureHunt(id) {
                let self = this;
                fetch(`/v1/treasureHunts/` + id, {
                    method: 'DELETE'
                }).then(function(response) {
                    if (response.ok) {
                        console.log("yes");
                    }
                    else {
                        console.log("No");
                    }
                });
            },


            /**
             * Runs a query which searches through the treasure hunts in the database and returns all.
             *
             * @returns {Promise<Response | never>}
             */
            queryTreasureHunts() {

                return fetch(`/v1/treasureHunts`, {
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        for (let i = 0; i < data.length; i++) {
                            this.foundTreasureHunts.push(data[i]);
                        }
                        this.loadingResults = false;
                    });
            },


            /**
             * Runs a query which searches through the treasure hunts in the database and returns only
             * treasure hunts created by the profile.
             *
             * @param profile       the profile that owns the treasure hunts.
             *
             * @returns {Promise<Response | never>}
             */
            queryYourTreasureHunts(profile) {

                return fetch(`/v1/treasureHunts/` + profile.id, {
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        for (let i = 0; i < data.length; i++) {
                            this.foundTreasureHunts.push(data[i]);
                        }
                        this.loadingResults = false;
                    })
            },


            addTreasureHunt() {
              this.creatingHunt = true;
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
                console.log(error);
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
        },

        components: {
            AddTreasureHunt
        }
    }
</script>

<style scoped>

</style>
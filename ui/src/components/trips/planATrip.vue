<template>
    <div class="container">
        <h1 class="page_title">{{ heading }}</h1>
        <p class="page_title"><i>Book your next trip!</i></p>
        <b-alert v-model="nameAlert" variant="danger" dismissible>No Trip Name!</b-alert>
        <b-alert v-model="destinationsAlert" variant="danger" dismissible>Must be at least 2 destinations</b-alert>

        <b-alert
                :show="dismissCountDown"
                dismissible
                variant="success"
                @dismissed="dismissCountDown=0"
                @dismiss-count-down="countDownChanged"
        >
            <p>Trip Successfully Added</p>
            <b-progress
                    variant="success"
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
            ></b-progress>
        </b-alert>
        <b-modal ref="editModal" id="editModal" hide-footer title="Edit Destination">
            <div class="d-block">
                <b-form-group id="editInDate-field" label="In Date:" label-for="editInDate">
                    <b-input id="editInDate" v-model="editInDate">{{editInDate}} trim</b-input>
                </b-form-group>
                <b-form-group id="editOutDate-field" label="Out Date:" label-for="editOutDate">
                    <b-input id="editOutDate" v-model="editOutDate">{{editOutDate}} trim</b-input>
                </b-form-group>
            </div>
            <b-button class="mr-2 float-right" variant="success" @click="saveDestination(rowEdit, editInDate, editOutDate); dismissModal; dismissCountDown">Save</b-button>
            <b-button class="mr-2 float-right" variant="danger" @click="dismissModal">Cancel</b-button>

        </b-modal>

        <b-form>
            <b-container fluid>
                <b-form-group
                        id="trip_name-field"
                        label="Trip Name:"
                        label-for="trip_name">
                    <b-form-input id="trip_name" v-model="tripName" :type="'text'" trim></b-form-input>
                </b-form-group>
            </b-container>
            <b-form @reset="resetDestForm">
                <b-container>
                    <b-row >
                        <b-col>
                            <b-form-group
                                    id="destination-field"
                                    label="Add a Destination:"
                                    label-for="destination">
                                <b-form-select id="destination" v-model="tripDestination" :type="'text'" trim>
                                    <option v-for="destination in destinations" :value="destination">{{destination.name}}</option>
                                </b-form-select>
                            </b-form-group>
                        </b-col>
                        <b-col>
                            <b-form-group
                                    id="inDate-field"
                                    label="In Date (optional):"
                                    label-for="inDate">
                                <b-form-input id="inDate" v-model="inDate" :type="'date'" trim></b-form-input>
                            </b-form-group>
                        </b-col>
                        <b-col>
                            <b-form-group
                                    id="outDate-field"
                                    label="Out Date (optional):"
                                    label-for="outDate">
                                <b-form-input id="outDate" v-model="outDate" :type="'date'" trim></b-form-input>
                            </b-form-group>
                        </b-col>
                    </b-row>
                    <b-button class="mr-2 float-right" variant="primary" @click="addDestination">Add Destination</b-button>
                </b-container>

            </b-form>

        </b-form>

        <b-table hover striped outlined
                 id="myTrips"
                 :fields="fields"
                 :items="tripDestinations"
                 :per-page="perPage"
                 :current-page="currentPage"
        >

            <template slot="actions" slot-scope="row">
                <b-button size="sm" v-b-modal.editModal @click="populateModal(row.item)" class="mr-2">Edit</b-button>
                <b-button size="sm" @click="row.toggleDetails" class="mr-2">
                    {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                </b-button>
                <b-button size="sm" @click="deleteDestination(row.item)" variant="danger" class="mr-2">Delete
                </b-button>
            </template>
            <template slot="row-details" slot-scope="row">
                <b-card>
                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>Type:</b></b-col>
                        <b-col>{{ row.item.type }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>District:</b></b-col>
                        <b-col>{{ row.item.district }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>Latitude:</b></b-col>
                        <b-col>{{ row.item.latitude }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>Longitude:</b></b-col>
                        <b-col>{{ row.item.longitude }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>Country:</b></b-col>
                        <b-col>{{ row.item.country }}</b-col>
                    </b-row>

                </b-card>
            </template>
        </b-table>
        <b-row>
            <b-col cols="1">
                <b-form-group
                        id="numItems-field"
                        label-for="perPage">
                    <b-form-select id="perPage" v-model="perPage" :options="optionViews" size="sm" trim> </b-form-select>
                </b-form-group>
            </b-col>
            <b-col cols="8">
                <b-pagination
                        v-model="currentPage"
                        :total-rows="rows"
                        :per-page="perPage"
                        aria-controls="my-table"
                        first-text="First"
                        last-text="Last"
                        align="center"
                        size="sm"
                ></b-pagination>
            </b-col>
        </b-row>
        <b-button variant="primary" block class="mr-2 float-right" @click="submitTrip">Save Trip</b-button>
    </div>
</template>

<script>
    export default {
        name: "PlanATrip",
        props: ['destinations'],
        data() {
            return {
                heading: 'Plan a Trip',
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPage: 10,
                currentPage: 1,
                tripName: null,
                tripDestination: "",
                inDate: "",
                outDate: "",
                nameAlert: false,
                destinationsAlert: false,
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                rowEdit: null,
                editName: null,
                editInDate: null,
                editOutDate: null,
                fields: [
                    { key: 'trip_name'},
                    { key: 'in_date' },
                    { key: 'out_date' },
                    'actions'
                ],
                subFields: [
                    {key: 'type'},
                    {key: 'district'},
                    {key: 'latitude'},
                    {key: 'longitude'},
                    {key: 'country'},


                ],
                tripDestinations: [

                ],

            }
        },
        computed: {
            rows() {
                return this.destinations.length
            }
        },
        methods: {
            addDestination: function() {
                this.tripDestinations.push({trip_name: this.tripDestination.name, type: this.tripDestination.type.destinationType, district: this.tripDestination.district, latitude: this.tripDestination.latitude, longitude: this.tripDestination.longitude, country: this.tripDestination.country, in_date: this.inDate,out_date: this.outDate});
                this.resetDestForm();
            },
            deleteDestination: function(row) {
                console.log(row);
                this.destinations.splice(1, row)
            },
            populateModal(row) {
                this.rowEdit = row;
                this.editInDate = row.in_date;
                this.editOutDate = row.out_date;
            },
            saveDestination(row, editInDate, editOutDate) {
                row.in_date = editInDate;
                row.out_date = editOutDate;
                this.$refs['editModal'].hide()
            },
            dismissModal() {
                this.$refs['editModal'].hide()
            },
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },
            resetDestForm() {
                this.tripDestination = "";
                this.inDate = "";
                this.outDate = "";
            },
            submitTrip: function() {
                if (this.tripName === null || this.tripName.length === 0) {
                    this.destinationsAlert = false;
                    this.nameAlert = true;
                } else if (this.destinations.length < 2) {
                    this.nameAlert = false;
                    this.destinationsAlert = true;
                } else {
                    this.nameAlert = false;
                    this.destinationsAlert = false;
                    this.showAlert();
                    let trip = {
                        name: this.tripName,
                        tripDestinations: this.destinations
                    };
                    this.resetDestForm();
                    this.tripName = "";
                    this.destinations = [];
                    console.log(trip);
                }

            }
        }
    }
</script>

<style scoped>

</style>
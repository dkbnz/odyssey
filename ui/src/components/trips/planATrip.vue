<template>
    <div class="container">
        <h1 class="page_title">{{ heading }}</h1>
        <p class="page_title"><i>Book your next trip!</i></p>
        <b-alert v-model="showError" variant="danger" dismissible>{{errorMessage}}</b-alert>

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
                    <b-input id="editInDate" :type="'date'" v-model="editInDate" max='9999-12-31'>{{editInDate}} trim</b-input>
                </b-form-group>
                <b-form-group id="editOutDate-field" label="Out Date:" label-for="editOutDate">
                    <b-input id="editOutDate" :type="'date'" v-model="editOutDate" max='9999-12-31'>{{editOutDate}} trim</b-input>
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
                                <b-form-input id="inDate" v-model="inDate" :type="'date'" max='9999-12-31' trim></b-form-input>
                            </b-form-group>
                        </b-col>
                        <b-col>
                            <b-form-group
                                    id="outDate-field"
                                    label="Out Date (optional):"
                                    label-for="outDate">
                                <b-form-input id="outDate" v-model="outDate" :type="'date'" max='9999-12-31' trim></b-form-input>
                            </b-form-group>
                        </b-col>
                    </b-row>
                    <b-button class="mr-2 float-right" variant="primary" @click="checkDestination">Add Destination</b-button>
                </b-container>

            </b-form>

        </b-form>

        <b-table hover striped outlined
                 ref="tripDestTable"
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
                <b-button size="sm" @click="deleteDestination(row.item, row.index)" variant="danger" class="mr-2">Delete
                </b-button>
            </template>
            <template slot="order" slot-scope="row">
                <b-button size="sm" :disabled="tripDestinations.length === 1 || row.index === 0" @click="moveUpCheck(row.index)" variant="success" class="mr-2">&uarr;
                </b-button>
                <b-button size="sm" :disabled="tripDestinations.length === 1 || row.index === tripDestinations.length-1" @click="moveDownCheck(row.index)" variant="success" class="mr-2">&darr;
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
        <b-button variant="primary" block class="mr-2 float-right" @click="submitTrip"> <b-spinner small v-if="savingTrip" variant="dark" label="Spinning">Saving...</b-spinner> Save Trip</b-button>


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
                showError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                rowEdit: null,
                editInDate: null,
                editOutDate: null,
                fields: [
                    'order',
                    { key: 'name'},
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
                tripDestinations: [],
                savingTrip: false,
                letTripSaved: false

            }
        },
        computed: {
            rows() {
                return this.tripDestinations.length
            }
        },
        methods: {
            checkDestination: function() {
                if (this.tripDestination) {
                    let startDate = new Date(this.inDate);
                    let endDate = new Date(this.outDate);

                    if(startDate > endDate) {
                        this.showError = true;
                        this.errorMessage = "Incorrect date ordering.";

                    } else if(this.tripDestinations.length === 0) {
                        this.addDestination()
                    } else if(!this.checkSameDestination(this.tripDestination.id)) {
                        this.addDestination()
                    } else {
                        this.showDuplicateDestError("add");
                    }
                } else {
                    this.showError = true;
                    this.errorMessage = "No Destination Selected";
                }

            },
            addDestination() {
                this.showError = false;
                this.tripDestinations.push({destId: this.tripDestination.id, name: this.tripDestination.name, type: this.tripDestination.type.destinationType, district: this.tripDestination.district, latitude: this.tripDestination.latitude, longitude: this.tripDestination.longitude, country: this.tripDestination.country, in_date: this.inDate,out_date: this.outDate});
                this.resetDestForm();
            },
            deleteDestination: function(row, rowIndex) {
                if(this.tripDestinations.length > 2) {
                    if (rowIndex === this.tripDestinations.length - 1) {
                        this.tripDestinations.splice(rowIndex, 1);
                    }
                    else if (this.tripDestinations[rowIndex - 1].destId !== this.tripDestinations[rowIndex + 1].destId) {
                        this.tripDestinations.splice(rowIndex, 1);
                    } else {
                        this.showDuplicateDestError("delete")
                    }
                } else {
                    this.tripDestinations.splice(rowIndex, 1);
                }
            },
            populateModal(row) {
                this.rowEdit = row;
                this.editInDate = row.in_date;
                this.editOutDate = row.out_date;
            },
            moveUpCheck(rowIndex) {
                if(rowIndex === 1 && this.tripDestinations[rowIndex-1].destId === this.tripDestinations[rowIndex+1].destId) {
                    this.showDuplicateDestError("move")
                }
                else if (rowIndex === 1) {
                    this.moveUp(rowIndex);
                }
                else if (rowIndex === this.getDestinationRows()-1) {
                    this.moveUp(rowIndex);
                }
                else if (this.tripDestinations[rowIndex-1].destId === this.tripDestinations[rowIndex+1].destId) {
                    this.showDuplicateDestError("move")
                }
                else if(this.tripDestinations[rowIndex-2].destId !== this.tripDestinations[rowIndex].destId) {
                    this.moveUp(rowIndex);
                } else {
                    this.showDuplicateDestError("move")
                }
            },
            moveUp(rowIndex) {
                let upIndex = rowIndex - 1;
                let swapRow = this.tripDestinations[rowIndex];
                this.tripDestinations[rowIndex] = this.tripDestinations[upIndex];
                this.tripDestinations[upIndex] = swapRow;
                this.$refs.tripDestTable.refresh()
            },
            moveDownCheck(rowIndex) {
                if(rowIndex === this.getDestinationRows()-2 && this.tripDestinations[rowIndex+1].destId === this.tripDestinations[rowIndex-1].destId) {
                    this.showDuplicateDestError("move")
                }
                else if (rowIndex === this.getDestinationRows()-2) {
                    this.moveDown(rowIndex);
                }
                else if (rowIndex === 0) {
                    this.moveDown(rowIndex);
                }
                else if (this.tripDestinations[rowIndex+1].destId === this.tripDestinations[rowIndex-1].destId) {
                    this.showDuplicateDestError("move")
                }
                else if(this.tripDestinations[rowIndex+2].destId !== this.tripDestinations[rowIndex].destId) {
                    this.moveDown(rowIndex);
                } else {
                    this.showDuplicateDestError("move")
                }
            },
            moveDown(rowIndex) {
                let upIndex = rowIndex + 1;
                let swapRow = this.tripDestinations[rowIndex];
                this.tripDestinations[rowIndex] = this.tripDestinations[upIndex];
                this.tripDestinations[upIndex] = swapRow;
                this.$refs.tripDestTable.refresh()
            },
            showDuplicateDestError(error) {
                this.showError = true;
                this.errorMessage = "Can't have same destination next to another, please choose another destination to " + error;
            },
            checkSameDestination(destId) {
                let previousDestinationIndex = this.tripDestinations.length - 1;
                if(this.tripDestinations[previousDestinationIndex].destId === destId) {
                    return true;
                }
            },
            saveDestination(row, editInDate, editOutDate) {
                row.in_date = editInDate;
                row.out_date = editOutDate;
                this.dismissModal();
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
                    this.showError = true;
                    this.errorMessage = "No Trip Name";
                } else if (this.tripDestinations.length < 2) {
                    this.showError = true;
                    this.errorMessage = "There must be at least 2 destinations";
                } else {
                    this.showError = false;
                    let tripDestinationsList = [];
                    for (let i = 0; i < this.tripDestinations.length; i++) {
                        if(this.tripDestinations[i].in_date === undefined || this.tripDestinations[i].in_date.length === 0) {
                            this.tripDestinations[i].in_date = null;
                        }
                        if(this.tripDestinations[i].out_date === undefined || this.tripDestinations[i].out_date.length === 0) {
                            this.tripDestinations[i].out_date = null;
                        }
                        tripDestinationsList.push({destination_id: this.tripDestinations[i].destId, start_date: this.tripDestinations[i].in_date, end_date: this.tripDestinations[i].out_date})
                    }
                    let trip = {
                        trip_name: this.tripName,
                        trip_destinations: tripDestinationsList
                    };
                    this.saveTrip(trip);
                }
            },
            saveTrip(trip) {
                this.savingTrip = true;
                let self = this;
                fetch('/v1/trip', {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(trip)
                }).then(function (response) {
                    if (response.ok) {
                        self.savingTrip = false;
                        self.showAlert();
                        self.$emit('tripSaved', true);
                        self.resetDestForm();
                        self.tripName = "";
                        self.tripDestinations = [];
                        return JSON.parse(JSON.stringify(response));
                    } else {
                        throw new Error('Something went wrong, try again later.');
                    }
                }).catch((error) => {
                    this.savingTrip = false;
                    this.showError = true;
                    this.errorMessage = (error);

                });
            },
            getDestinationRows() {
                return this.tripDestinations.length
            },
        }
    }
</script>

<style scoped>

</style>
<template>
    <div :class="containerClass">

        <h1 class="page-title">{{ heading }}</h1>
        <p class="page-title"><i>{{ subHeading }}</i></p>

        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <!-- Displays success alert and progress bar on trip creation as a loading bar
        for the trip being added to the database -->
        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success">
            <p>Trip Successfully Saved</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>

        <!-- Modal for editing the arrival and departure dates for a destination
        Displayed when the 'Edit' button is clicked on a destination -->
        <b-modal hide-footer id="editModal" ref="editModal" title="Edit Destination">
            <b-alert dismissible v-model="showDateError" variant="danger">{{errorMessage}}</b-alert>
            <div class="d-block">
                <b-form-group id="editInDate-field" label="Start Date:" label-for="editInDate">
                    <b-input :type="'date'"
                             id="editInDate"
                             max='9999-12-31'
                             v-model="editInDate">
                        {{editInDate}} trim
                    </b-input>
                </b-form-group>
                <b-form-group id="editOutDate-field" label="End Date:" label-for="editOutDate">
                    <b-input :type="'date'"
                             id="editOutDate"
                             max='9999-12-31'
                             v-model="editOutDate">
                        {{editOutDate}} trim
                    </b-input>
                </b-form-group>
            </div>

            <!-- Buttons to cancel/save edit -->
            <b-button @click="saveDestination(rowEdit, editInDate, editOutDate); dismissModal; dismissCountDown"
                      class="mr-2 float-right"
                      variant="success">
                Save
            </b-button>
            <b-button @click="dismissModal"
                      class="mr-2 float-right"
                      variant="danger">
                Cancel
            </b-button>
        </b-modal>

        <b-row>
            <b-col cols="8">
                <b-card>
                    <b-form>

                        <b-container fluid>
                            <b-form-group
                                    id="trip_name-field"
                                    label="Trip Name:"
                                    label-for="trip_name">
                                <b-form-input :type="'text'"
                                              id="trip_name"
                                              trim
                                              v-model="inputTrip.name"></b-form-input>
                            </b-form-group>
                        </b-container>

                        <!-- Form for adding a destination. Reset on destination add -->
                        <b-form @reset="resetDestForm">
                            <b-container fluid>
                                <b-row>
                                    <b-col>
                                        <h6 class="mb-1">Selected Destination:</h6>
                                        <b-list-group>
                                            <b-list-group-item href="#" class="flex-column align-items-start"
                                                               id="selectedDestination"
                                                               :disabled="selectedDestination.length === '{}'">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1" v-if="selectedDestination.name">
                                                        {{selectedDestination.name}}
                                                    </h5>
                                                    <h5 class="mb-1" v-else>Select a Destination</h5>

                                                    <small>
                                                        <div class="d-flex justify-content-right">
                                                            <b-button variant="primary"
                                                                      @click="checkDestination"
                                                                      v-if="selectedDestination.name">Add to Trip
                                                            </b-button>
                                                        </div>
                                                    </small>
                                                </div>

                                                <p>
                                                    {{selectedDestination.district}}
                                                </p>
                                                <p>
                                                    {{selectedDestination.country}}
                                                </p>
                                            </b-list-group-item>
                                        </b-list-group>
                                    </b-col>

                                    <b-col>
                                        <b-form-group
                                                id="inDate-field"
                                                label="In Date (optional):"
                                                label-for="inDate">
                                            <b-form-input :type="'date'"
                                                          id="inDate"
                                                          max='9999-12-31'
                                                          trim
                                                          v-model="inDate"></b-form-input>
                                        </b-form-group>
                                        <b-form-group
                                                id="outDate-field"
                                                label="Out Date (optional):"
                                                label-for="outDate">
                                            <b-form-input :type="'date'"
                                                          id="outDate"
                                                          max='9999-12-31'
                                                          trim
                                                          v-model="outDate"></b-form-input>
                                        </b-form-group>
                                    </b-col>


                                </b-row>

                                <!--<b-button @click="checkDestination"-->
                                          <!--class="mr-2 float-right"-->
                                          <!--variant="primary">-->
                                    <!--Add Destination-->
                                <!--</b-button>-->

                            </b-container>
                        </b-form>
                    </b-form>

                    <b-container fluid style="margin-top: 20px">
                        <!-- Table displaying all added destinations -->
                        <b-table :current-page="currentPage" :fields="fields" :items="inputTrip.destinations"
                                 :per-page="perPage"
                                 hover
                                 id="myTrips"
                                 outlined
                                 ref="tripDestTable"
                                 striped>

                            <!-- Buttons that appear for each destination added to table -->
                            <template slot="actions" slot-scope="row">
                                <!--Opens edit modal-->
                                <b-button size="sm"
                                          v-b-modal.editModal
                                          @click="populateModal(row.item)"
                                          variant="success"
                                          class="mr-2"
                                          block>Edit
                                </b-button>
                                <!-- Shows additional details about the selected destination -->
                                <b-button size="sm"
                                          @click="row.toggleDetails"
                                          variant="warning"
                                          class="mr-2"
                                          block>
                                    {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                                </b-button>
                                <!--Removes destination from table-->
                                <b-button size="sm"
                                          @click="deleteDestination(row.index)"
                                          variant="danger"
                                          class="mr-2"
                                          block>Delete
                                </b-button>
                            </template>

                            <!-- Buttons to shift destinations up/down in table -->
                            <template slot="order" slot-scope="row">
                                <b-button :disabled="inputTrip.destinations.length === 1 || row.index === 0"
                                          @click="moveUp(row.index)"
                                          class="mr-2"
                                          size="sm"
                                          variant="success">&uarr;
                                </b-button>
                                <b-button :disabled="inputTrip.destinations.length === 1 ||
                           row.index === inputTrip.destinations.length-1"
                                          @click="moveDown(row.index)"
                                          class="mr-2"
                                          size="sm"
                                          variant="success">&darr;
                                </b-button>
                            </template>

                            <!-- Additional details about selected destination, shown when 'Show Details' button is clicked -->
                            <template slot="row-details" slot-scope="row">
                                <b-card>
                                    <b-row class="mb-2">
                                        <b-col class="text-sm-right" sm="3"><b>Type:</b></b-col>
                                        <b-col>{{ row.item.destination.type.destinationType }}</b-col>
                                    </b-row>

                                    <b-row class="mb-2">
                                        <b-col class="text-sm-right" sm="3"><b>District:</b></b-col>
                                        <b-col>{{ row.item.destination.district }}</b-col>
                                    </b-row>

                                    <b-row class="mb-2">
                                        <b-col class="text-sm-right" sm="3"><b>Latitude:</b></b-col>
                                        <b-col>{{ row.item.destination.latitude }}</b-col>
                                    </b-row>

                                    <b-row class="mb-2">
                                        <b-col class="text-sm-right" sm="3"><b>Longitude:</b></b-col>
                                        <b-col>{{ row.item.destination.longitude }}</b-col>
                                    </b-row>

                                    <b-row class="mb-2">
                                        <b-col class="text-sm-right" sm="3"><b>Country:</b></b-col>
                                        <b-col>{{ row.item.destination.country }}</b-col>
                                    </b-row>
                                </b-card>
                            </template>

                        </b-table>
                        <!-- Determines pagination and number of results per row of the table -->
                        <b-row>
                            <b-col cols="1">
                                <b-form-group
                                        id="numItems-field"
                                        label-for="perPage">
                                    <b-form-select :options="optionViews"
                                                   id="perPage"
                                                   size="sm"
                                                   trim v-model="perPage">
                                    </b-form-select>
                                </b-form-group>
                            </b-col>
                            <b-col cols="8">
                                <b-pagination
                                        :per-page="perPage"
                                        :total-rows="rows"
                                        align="center"
                                        aria-controls="my-table"
                                        first-text="First"
                                        last-text="Last"
                                        size="sm"
                                        v-model="currentPage">
                                </b-pagination>
                            </b-col>
                        </b-row>
                        <b-button @click="validateTrip"
                                  block class="mr-2 float-right"
                                  variant="primary">
                            <b-spinner label="Spinning"
                                       small
                                       v-if="savingTrip"
                                       variant="dark">
                                Saving...
                            </b-spinner>
                            Save Trip
                        </b-button>
                    </b-container>
                </b-card>
            </b-col>
            <b-col>
                <b-card>
                    <destination-sidebar
                            :profile="profile"
                            @destination-click="destination => this.selectedDestination = destination"
                            @data-changed="$emit('data-changed')"
                    ></destination-sidebar>
                </b-card>
            </b-col>
        </b-row>

    </div>
</template>


<script>
    import DestinationSidebar from "../destinations/destinationSidebar";
    export default {
        name: "PlanATrip",
        components: {DestinationSidebar},
        props: {
            profile: Object,
            inputTrip: {
                default: function () {
                    return {
                        id: null,
                        name: "",
                        destinations: []
                    }
                }
            },
            heading: String,
            subHeading: String,
            containerClass: {
                default: function() {
                    return 'containerWithNav';
                }
            }
        },
        data() {
            return {
                optionViews: [{value: 1, text: "1"}, {value: 5, text: "5"}, {value: 10, text: "10"}, {
                    value: 15,
                    text: "15"
                }],
                perPage: 10,
                currentPage: 1,
                tripDestination: "",
                inDate: "",
                outDate: "",
                showError: false,
                showDateError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                rowEdit: null,
                editInDate: null,
                editOutDate: null,
                fields: [
                    'order',
                    {key: 'destination.name', label: 'Destination Name'},
                    {key: 'startDate'},
                    {key: 'endDate'},
                    'actions'
                ],
                subFields: [
                    {key: 'destination.type.destinationType'},
                    {key: 'destination.district'},
                    {key: 'destination.latitude'},
                    {key: 'destination.longitude'},
                    {key: 'destination.country'},
                ],
                savingTrip: false,
                letTripSaved: false,
                destinationsList: [],
                selectedDestination: {}
            }
        },
        computed: {
            /**
             * Computed function used for the pagination of the table.
             * @returns {number} the number of rows required in the table based on number of destinations to be
             * displayed.
             */
            rows() {
                return this.inputTrip.destinations.length
            }
        },
        mounted() {
            this.getDestinations(destinationsList => this.destinationsList = destinationsList);
        },
        methods: {
            /**
             * Method used to check the destination to be added to the table is valid.
             * First insures a destination is selected from the list.
             * Then ensures the start date is before the end date.
             */
            checkDestination() {
                if (this.selectedDestination) {
                    let startDate = new Date(this.inDate);
                    let endDate = new Date(this.outDate);

                    if (startDate < endDate) {
                        this.addDestination()
                    } else if (this.inDate.length === 0 || this.outDate.length === 0) {
                        this.addDestination()
                    } else {
                        this.showError = true;
                        this.errorMessage = "Incorrect date ordering.";
                    }
                } else {
                    this.showError = true;
                    this.errorMessage = "No Destination Selected";
                }
            },


            /**
             * Method to add the destination to the list of trip destinations. This also adds the destination to the
             * table so the destination can be reordered or edited. After the destination is added, the form for adding
             * a destination is reset/cleared.
             */
            addDestination() {
                this.showError = false;
                this.inputTrip.destinations.push({
                    destination: {
                        id: this.selectedDestination.id,
                        name: this.selectedDestination.name,
                        type: {
                            destinationType: this.selectedDestination.type.destinationType
                        },
                        district: this.selectedDestination.district,
                        latitude: this.selectedDestination.latitude,
                        longitude: this.selectedDestination.longitude,
                        country: this.selectedDestination.country
                    },
                    startDate: this.inDate,
                    endDate: this.outDate
                });
                this.resetDestForm();
            },


            /**
             * Used after the destination is added, resets the form for adding a destination.
             */
            resetDestForm() {
                this.selectedDestination = {};
                this.tripDestination = "";
                this.inDate = "";
                this.outDate = "";
            },


            /**
             * Method to delete a destination from the list of trip destinations.
             *
             * @param rowIndex      the index of the row in the table
             */
            deleteDestination(rowIndex) {
                this.inputTrip.destinations.splice(rowIndex, 1);
            },


            /**
             * Used to populate the modal to edit a destination dates in the trip.
             *
             * @param row           the selected row (destination) in the table.
             */
            populateModal(row) {
                this.rowEdit = row;
                this.editInDate = row.startDate;
                this.editOutDate = row.endDate;
            },


            /**
             * Used to save the destination that is being edited.
             * Checks the start date is before the end date.
             *
             * @param row           the row (destination) being edited.
             * @param editInDate    the in date being edited.
             * @param editOutDate   the out date being edited.
             */
            saveDestination(row, editInDate, editOutDate) {
                if (editInDate === null || editOutDate === null || editInDate === ""
                    || editOutDate === "" || editInDate <= editOutDate) {
                    this.showDateError = false;
                    if (editInDate === "") {
                        editInDate = null;
                    }
                    if (editOutDate === "") {
                        editOutDate = null;
                    }
                    row.startDate = editInDate;
                    row.endDate = editOutDate;
                    this.dismissModal();
                } else {
                    this.showDateError = true;
                    this.errorMessage = "Can't have the out date after the in date!"
                }
            },


            /**
             * Used to dismiss the modal used to edit the destination.
             */
            dismissModal() {
                this.$refs['editModal'].hide()
            },


            /**
             * Used to move a destination in the table up one place. The trip isn't checked for duplicates until it is
             * saved.
             *
             * @param rowIndex      the row index of the destination in the table.
             */
            moveUp(rowIndex) {
                let upIndex = rowIndex - 1;
                let swapRow = this.inputTrip.destinations[rowIndex];
                this.inputTrip.destinations[rowIndex] = this.inputTrip.destinations[upIndex];
                this.inputTrip.destinations[upIndex] = swapRow;
                this.$refs.tripDestTable.refresh();
            },


            /**
             * Used to move a destination in the table down one place. The trip isn't checked for duplicates until it is
             * saved.
             *
             * @param rowIndex      the row index of the destination in the table.
             */
            moveDown(rowIndex) {
                let downIndex = rowIndex + 1;
                let swapRow = this.inputTrip.destinations[rowIndex];
                this.inputTrip.destinations[rowIndex] = this.inputTrip.destinations[downIndex];
                this.inputTrip.destinations[downIndex] = swapRow;
                this.$refs.tripDestTable.refresh();
            },


            /**
             * Used to check the trip trying to be saved is valid.
             * A valid trip has a name, has at least two destinations, has no duplicate destinations next to each other,
             * each destination's dates are chronologically valid compared to its previous dates.
             * If the trip is invalid, errors are shown. If the trip is valid then it is either given to the saveNewTrip
             * method or the saveOldTrip method depending on if the trip is a new trip, or the trip is an old trip being
             * edited.
             */
            validateTrip() {
                if (this.inputTrip.name === null || this.inputTrip.name.length === 0) {
                    this.showError = true;
                    this.errorMessage = "No Trip Name";
                } else if (this.inputTrip.destinations.length < 2) {
                    this.showError = true;
                    this.errorMessage = "There must be at least 2 destinations";
                } else if (this.checkDuplicateDestinations()) {
                    this.showDuplicateDestError("save");
                } else if (!this.checkValidDestinationDates()) {
                    this.showError = true;
                    this.errorMessage = "The ordering of the dates doesn't work!";
                } else {
                    this.showError = false;
                    let tripDestinationsList = [];
                    for (let i = 0; i < this.inputTrip.destinations.length; i++) {
                        tripDestinationsList.push({
                            destination_id: this.inputTrip.destinations[i].destination.id,
                            start_date: this.inputTrip.destinations[i].startDate,
                            end_date: this.inputTrip.destinations[i].endDate
                        })
                    }
                    let trip = {
                        trip_name: this.inputTrip.name,
                        trip_destinations: tripDestinationsList
                    };
                    if (this.inputTrip.id === null) {
                        this.saveNewTrip(trip);
                    } else {
                        this.saveOldTrip(trip, this.inputTrip.id);
                    }
                }
            },


            /**
             * Used to check if there are duplicate destinations next to one another in a trip upon the trip save.
             *
             * @returns {boolean}, true if there is duplicates, false otherwise.
             */
            checkDuplicateDestinations() {
                let result = [];
                for (let i = 0; i < this.inputTrip.destinations.length - 1; i++) {
                    if (this.inputTrip.destinations[i].destination.id ===
                        this.inputTrip.destinations[i + 1].destination.id) {
                        result.push(true);
                    } else {
                        result.push(false);
                    }
                }
                if (result.includes(true)) {
                    return true;
                }
            },


            /**
             * Used to show an alert saying there are duplicate destinations next to one another in the trip.
             *
             * @param error The error message to be displayed
             */
            showDuplicateDestError(error) {
                this.showError = true;
                this.errorMessage = "Can't have same destination next to another, please choose another destination to "
                    + error;
            },


            /**
             * Checks all the destination dates in a trip to ensure that the end date of a destination is before its
             * following destination start date, or if the dates are null.
             *
             * @returns {boolean} true if the dates are valid, false otherwise.
             */
            checkValidDestinationDates() {
                let destinationList = this.inputTrip.destinations;
                let dateList = [];
                let previousDate = null;
                // Put all dates into a queue based array.
                for (let i = 0; i < destinationList.length; i++) {
                    dateList.push(destinationList[i].startDate);
                    dateList.push(destinationList[i].endDate);
                }

                // Go through the list sequentially to see if the dates are in order.
                for (let i = 0; i < dateList.length; i++) {
                    if (dateList[i] !== null && dateList[i].length > 1) {
                        if (dateList[i] < previousDate) {
                            return false;
                        } else {
                            previousDate = dateList[i];
                        }
                    }
                }
                // All good, return true
                return true;
            },


            /**
             * Uses a fetch method (POST) to save a new trip. If there is an error for some reason, this is shown to the
             * user.
             * If the trip is successfully saved, then an alert is shown to the user and the trip form is reset.
             *
             * @param trip      the trip to be saved.
             */
            saveNewTrip(trip) {
                this.savingTrip = true;
                let self = this;
                fetch('/v1/trips/' + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(trip)
                }).then(function (response) {
                    if (response.status === 201) {
                        self.savingTrip = false;
                        self.showAlert();
                        self.$emit('tripSaved', true);
                        self.resetDestForm();
                        self.inputTrip.name = "";
                        self.inputTrip.destinations = [];
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


            /**
             * Uses a fetch method (PATCH) to save an old trip. If there is an error for some reason, this is shown to
             * the user.
             * If the trip is successfully saved, then an alert is shown to the user and the trip form is reset.
             *
             * @param trip      the trip to be saved
             * @param tripId    the id of the trip to be saved. This is required because the trip is being edited.
             */
            saveOldTrip(trip, tripId) {
                this.savingTrip = true;
                let self = this;
                fetch('/v1/trips/' + tripId, {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(trip)
                }).then(function (response) {
                    if (response.status === 200) {
                        self.savingTrip = false;
                        self.showAlert();
                        self.$emit('tripSaved', true);
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


            /**
             * Displays the countdown alert on the successful saving of a trip.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },


            /**
             * Used to allow an alert to countdown on the successful saving of a trip.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },


            /**
             * Retrieves the list of all destinations.
             *
             * @param updateDestinations    the list of destinations to be updated.
             */
            getDestinations(updateDestinations) {
                return fetch(`/v1/destinations`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateDestinations);
            },


            /**
             * Converts the retrieved Http response to a Json format.
             *
             * @param response the Http response.
             * @returns the Http response body as Json.
             */
            parseJSON(response) {
                return response.json();
            },


            /**
             * Sorts the destinations based on the name of the destination.
             *
             * @param first      for each destination, current destination
             * @param next       for each destination, next destination
             * @returns {number} depending on if destinations should be swapped or not -1, 1, 0
             */
            compare(first, next) {
                const nameFirst = first.name.toUpperCase(); // ignore upper and lowercase
                const nameSecond = next.name.toUpperCase(); // ignore upper and lowercase

                if (nameFirst < nameSecond ) {
                    return -1;
                } else if (nameFirst > nameSecond ) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }
</script>
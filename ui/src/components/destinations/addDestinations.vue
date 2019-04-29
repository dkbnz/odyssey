<template>
    <div class="container">
        <h1 class="page_title">Add a Destination</h1>
        <p class="page_title"><i>Add a destination using the form below</i></p>
        <b-alert v-model="showError" variant="danger" dismissible>{{errorMessage}}</b-alert>
        <b-alert
                :show="dismissCountDown"
                dismissible
                variant="success"
                @dismissed="dismissCountDown=0"
                @dismiss-count-down="countDownChanged"
        >
            <p>Destination Successfully Added</p>
            <b-progress
                    variant="success"
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
            ></b-progress>
        </b-alert>
        <div>
            <b-form>
                <b-form-group
                        id="name-field"
                        label="Destination Name:"
                        label-for="dName">
                    <b-form-input id="dName" v-model="dName" type="text" required></b-form-input>
                </b-form-group>

                <b-form-group
                        id="type-field"
                        label="Destination Type:"
                        label-for="type">
                    <b-form-select id="type" v-model="dType" trim>
                        <option v-for="destination in destinationTypes" :value="destination.id">{{destination.destinationType}}</option>
                    </b-form-select>
                </b-form-group>

                <b-form-group
                        id="district-field"
                        label="District:"
                        label-for="district">
                    <b-form-input id="district" v-model="dDistrict" type="text" trim required></b-form-input>
                </b-form-group>

                <b-form-group
                        id="latitude-field"
                        label="Latitude:"
                        label-for="latitude">
                    <b-form-input id="latitude" v-model="dLatitude" type="text" trim required></b-form-input>
                </b-form-group>

                <b-form-group
                        id="longitude-field"
                        label="Longitude:"
                        label-for="longitude">
                    <b-form-input id="longitude" v-model="dLongitude" type="text" trim required></b-form-input>
                </b-form-group>

                <b-form-group
                        id="country-field"
                        label="Country:"
                        label-for="country">
                    <b-form-input id="country" v-model="dCountry" type="text" trim required></b-form-input>
                </b-form-group>

                <b-button block variant="primary" @click="checkDestinationFields">Add Destination</b-button>
            </b-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "addDestinations",
        props: ['profile', 'destinations', 'destinationTypes'],
        data () {
            return {
                dName: "",
                dType: "",
                dDistrict: "",
                dLatitude: null,
                dLongitude: null,
                dCountry: "",
                showError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0
            }
        },
        methods: {
            checkLatLong() {
                let ok = true;
                if (isNaN(this.dLatitude)) {
                    this.showError = true;
                    this.errorMessage = ("Latitude: '" + this.dLatitude + "' is not a number!");
                    ok = false;
                } else if (isNaN(this.dLongitude)) {
                    this.showError = true;
                    this.errorMessage = ("Longitude: '" + this.dLongitude + "' is not a number!");
                    ok = false;
                }
                return ok;
            },
            checkDestinationFields() {
                if (this.checkLatLong() && this.dName && this.dDistrict && this.dLatitude && this.dLongitude && this.dCountry) {
                    this.showError = false;
                    this.addDestination();
                }
            },
            resetDestForm() {
                this.dName = "";
                this.dType = "";
                this.dDistrict = "";
                this.dLatitude = "";
                this.dLongitude = "";
                this.dCountry = "";
            },
            addDestination (cb) {
                let self = this;
                let response = fetch(`/v1/destinations`, {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: (JSON.stringify({"name": this.dName, "type_id": this.dType, "district": this.dDistrict, "latitude": parseFloat(this.dLatitude), "longitude": parseFloat(this.dLongitude), "country": this.dCountry}))
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb)

                    .then(function(response) {
                        if (response.ok) {
                            self.resetDestForm();
                            self.showAlert();
                            return JSON.parse(JSON.stringify(response));
                        } else {
                            throw new Error('Something is wrong!');
                        }
                })
                    .catch(() => {
                        this.showError = true;
                        this.errorMessage = ("'" + this.dName + "' already exists as a destination!");

                    });

                return response;
            },
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },
        }
    }
</script>

<style scoped>

</style>
<template>
    <div class="container">
        <h1 class="page_title">Add a Destination</h1>
        <p class="page_title"><i>Add a destination using the form below</i></p>
        <b-alert v-model="showError" variant="danger" dismissible>{{errorMessage}}</b-alert>
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
                    <b-form-select id="type" v-model="dType"trim>
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

                <b-button type="submit" block variant="primary" @click="checkDestinationFields">Add Destination</b-button>
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
                dType: "AMENITY_AREA",
                dDistrict: "",
                dLatitude: null,
                dLongitude: null,
                dCountry: "",
                showError: false,
                errorMessage: "",
                newDestination: ""
            }
        },
        methods: {
            checkDestinationName() {
                let ok = true;
                for (let i=0; i < this.destinations.length; i++) {
                    if (this.dName.toUpperCase() === this.destinations[i].name.toUpperCase()) {
                        this.showError = true;
                        this.errorMessage = ("'" + this.dName + "' already exists as a destination!");
                        ok = false;
                    }
                }
                return ok;
            },
            checkLatLong() {
                let ok = true;
                if (isNaN(this.dLatitude)) {
                    this.showError = true;
                    this.errorMessage = ("'" + this.dLatitude + "' is not a number!");
                    ok = false;
                } else if (isNaN(this.dLongitude)) {
                    this.showError = true;
                    this.errorMessage = ("'" + this.dLongitude + "' is not a number!");
                    ok = false;
                }
                return ok;
            },
            checkDestinationFields() {
                if (this.checkDestinationName() && this.checkLatLong() && this.dName && this.dDistrict && this.dLatitude && this.dLongitude && this.dCountry) {
                    this.showError = false;
                    this.newDestination = {'name': this.dName, 'type': this.dType, 'district': this.dDistrict, 'latitude': parseFloat(this.dLatitude), 'longitude': parseFloat(this.dLongitude), 'country': this.dCountry};
                    this.addDestination();
                }
            },
            addDestination (cb) {
                return fetch(`/v1/destinations`, {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify((this.newDestination))
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            }
        }
    }
</script>

<style scoped>

</style>
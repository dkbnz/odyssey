<template>
    <div>
        <b-form-file multiple :file-name-formatter="formatNames" accept="image/*"
                v-model="files"
                :state="Boolean(files)"
                placeholder="Choose files..."
                drop-placeholder="Drop files here..."
        ></b-form-file>
        <b-button class="btn btn-info btn-lg" @click="files = null">Clear Selected Files</b-button>
        <b-button class="btn btn-info btn-lg" @click="save()">Save</b-button>
    </div>
</template>

<script>
    export default {
        name: "photoUploader",
        data: function() {
            return {
                files: null
            }
        },
        methods: {
            formatNames(files) {
                if (files.length === 1) {
                    return files[0].name
                } else {
                    return `${files.length} images selected`
                }
            },
            save(){
                this.$emit('save-photos', this.files);
                this.files = null;
                }
            }

    }
</script>

<style scoped>

</style>
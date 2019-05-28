<template>
    <div>
        <!--Bootstrap Vue form file uploader for the user to select the files to upload-->
        <b-form-file @change="$emit('dismiss-error')" v-if="multipleFiles" :file-name-formatter="formatNames" :accept="acceptTypes"
                     drop-placeholder="Drop files here..."
                     multiple
                     placeholder="Choose files..."
                     v-model="files"
        ></b-form-file>
        <b-form-file @change="$emit('dismiss-error')" v-else :file-name-formatter="formatNames" :accept="acceptTypes"
                     drop-placeholder="Drop file here..."
                     placeholder="Choose file..."
                     v-model="files"
        ></b-form-file>
        <b-button @click="files = null" class="btn btn-info">Clear Selected Files</b-button>
        <b-button :disabled="files == null || files.length === 0" @click="save()" class="btn btn-info">Save</b-button>
    </div>
</template>

<script>
    export default {
        name: "photoUploader",
        props: {
            acceptTypes: String,
            multipleFiles: {
                default: function () {
                    return true;
                }
            }
        },
        data() {
            return {
                files: null
            }
        },
        methods: {

            /**
             * Return the number of files selected by the file uploader.
             * If there is exactly one file selected, return the name.
             *
             * @param files
             * @returns {*}
             */
            formatNames(files) {
                if (files.length === 1) {
                    return files[0].name
                } else {
                    return `${files.length} images selected`
                }
            },

            /**
             * Emit the saved files to the parent component.
             */
            save() {
                this.$emit('save-photos', this.files);
                this.files = null;
            }
        }
    }
</script>

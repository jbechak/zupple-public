<template>
  <div :class="STYLES.TABLE_CONTAINER">
    <table :class="STYLES.TABLE">
      <thead>
        <tr>
          <TableHeader
            v-for="(col, index) in columns[puzzleType]" :key="index"
            :listingData="listingData"
            :column="col.sortField ?? col.field"
            :label="col.header"
            :isNumber="col.isNumber"
            :puzzleType="puzzleType"
          />
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(record, index) in listingData" :key="index">
          <td 
            v-for="(col, index) in columns[puzzleType]" 
            :key="index" 
            :class="getDataClass(col.isNumber)"
          >
            {{ record[col.field] }}
          </td>
          <ListTableButtons
            :record="record"
            @edit="emit('edit', record)"
            @view="emit('view', record)"
            @delete="emit('delete', record)"
          />
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import ListTableButtons from '@/components/ListTableButtons.vue';
import { columns } from '@/helpers';
import { STYLES } from '@/constants';
import TableHeader from '@/components/TableHeader.vue';

const props = defineProps({
  puzzleType: { type: String, default: null },
  listingData: { type: Array, default: [] },
});
const emit = defineEmits(['edit', 'view', 'delete']);

const getDataClass = (isNumber) => isNumber ? [STYLES.TEXT_END, STYLES.PE_4] : null;
</script>
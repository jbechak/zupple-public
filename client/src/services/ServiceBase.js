import axios from "axios";
import { isReactive, unref } from "vue";
import { useQuery } from "vue-query";

export default class service {
  static get serviceRoot() {
    throw new Error("service root must be overriden in subclass");
  }

  static async get(id) {
    if (!id) throw new Error("'id' must be specified");
    const result = (await axios.get(`${this.serviceRoot}/${id}`));
    return result;
  }
  
  static useGet(id, options) {
    if (options != null && !isReactive(options))    
      throw new Error("'options' must be either reactive or null");

    return useQuery([this.serviceRoot], async () => {
      return (await this.get(unref(id))).data;
    }, options);
  }

  static async getAll() {
    const result = (await axios.get(this.serviceRoot));
    return result;
  }

  static useGetAll(options) {
    if (options != null && !isReactive(options))    
      throw new Error("'options' must be either reactive or null");

    return useQuery([this.serviceRoot], async () => {
      return (await this.getAll()).data;
    }, options);
  }

  static async generate(requestObject) {
    if (!requestObject) throw new Error("'requestObject' must be specified");
    const result = (await axios.post(`${this.serviceRoot}/generate`, requestObject)).data;
    return result;
  }
  
  static async create(requestObject) {
    if (!requestObject) throw new Error("'requestObject' must be specified");
    const result = (await axios.post(this.serviceRoot, requestObject)).data;
    return result;
  }
  
  static async update(requestObject) {
    if (!requestObject) throw new Error("'requestObject' must be specified");
    const result = (await axios.put(this.serviceRoot, requestObject)).data;
    return result;
  }

  static async delete(id) {
    if (!id) throw new Error("'id' must be specified");
    const result = (await axios.delete(`${this.serviceRoot}/${id}`));
    return result;
  }
}
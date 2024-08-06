"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const program_1 = require("services/program");
/**
 * Handles TSConfig file creation requests
 */
async function default_1(request, response, next) {
    try {
        const tsconfig = request.body;
        response.json(await (0, program_1.writeTSConfigFile)(tsconfig));
    }
    catch (error) {
        next(error);
    }
}
exports.default = default_1;
//# sourceMappingURL=on-create-tsconfig-file.js.map
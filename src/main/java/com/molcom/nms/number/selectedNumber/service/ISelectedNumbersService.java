package com.molcom.nms.number.selectedNumber.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;

import java.util.List;

public interface ISelectedNumbersService {
    GenericResponse<SelectedNumberModel> saveSelectedNumber(SelectedNumberModel selectedNumberModel) throws Exception;

    GenericResponse<List<SelectedNumberModel>> saveBulkSelectedNumber(List<SelectedNumberModel> selectedNumberModel) throws Exception;

    GenericResponse<SelectedNumberModel> deleteSelectedNumber(String selectedNumberId) throws Exception;

    GenericResponse<List<SelectedNumberModel>> getSelectedNumbers(String applicationId) throws Exception;

    GenericResponse<SelectedNumberModel> dropRejectedNumbers(String applicationId) throws Exception;

    GenericResponse<List<SelectedNumberModel>> replaceSelectedNumber(List<SelectedNumberModel> selectedNumberModel) throws Exception;

}

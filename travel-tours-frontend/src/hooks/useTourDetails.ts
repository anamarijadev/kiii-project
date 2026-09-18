import { useCallback, useEffect, useState } from 'react';
import useSnackbar from './useSnackbar.ts';
import type { DisplayTourDetailsResponse } from '../api/types/tour.ts';
import tourApi from '../api/tourApi.ts';
import { getErrorMessage } from '../api/getErrorMessage.ts';
import reservationApi from '../api/reservationApi.ts';

const useTourDetails = (id?: string) => {
  const { showSnackbar } = useSnackbar();

  const [tourDetails, setTourDetails] = useState<DisplayTourDetailsResponse | null>(null);
  const [availableSpots, setAvailableSpots] = useState<number | null>(null);
  const [isReserved, setIsReserved] = useState<boolean | null>(null);

  const fetch = useCallback(async () => {
    if (!id) {
      return;
    }

    try {
      const response = await tourApi.findWithDetailsById(Number(id));
      setTourDetails(response.data);
    } catch (err) {
      showSnackbar(getErrorMessage(err, 'Failed to load tour details.'), 'error');
    }

    try {
      const response = await reservationApi.getAvailableSpotsByTour(Number(id));
      setAvailableSpots(response.data);
    } catch (err) {
      showSnackbar(getErrorMessage(err, 'Failed to load available spots.'), 'error');
    }

    try {
      const response = await reservationApi.existsByTourAndUser(Number(id));
      setIsReserved(response.data);
    } catch (err) {
      showSnackbar(getErrorMessage(err, 'Failed to load reservation status.'), 'error');
    }
  }, [id, showSnackbar]);

  const reserve = useCallback(async () => {
    try {
      await reservationApi.reserveByTour(Number(id));
      await fetch();
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to reserve.', 'error');
    }
  }, [fetch, id, showSnackbar]);

  useEffect(() => {
    void fetch();
  }, [fetch]);

  return { tourDetails, availableSpots, isReserved, reserve };
};

export default useTourDetails;
